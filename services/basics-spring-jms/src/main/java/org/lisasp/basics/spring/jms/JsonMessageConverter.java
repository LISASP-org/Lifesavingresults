package org.lisasp.basics.spring.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * Converts json-text to and from objects
 */
@Slf4j
public class JsonMessageConverter implements MessageConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String messageFormat = "%s|%s";

    /**
     * Constructor
     */
    public JsonMessageConverter() {
        log.info("Initializing");
        mapper.findAndRegisterModules();
    }

    /**
     * Registers a jackson module to the internal mapper
     *
     * @param module Jackson module
     */
    public void registerModule(com.fasterxml.jackson.databind.Module module) {
        mapper.registerModule(module);
    }

    @Override
    public @NotNull Message toMessage(@NotNull Object value, @NotNull Session session) throws JMSException, MessageConversionException {
        try {
            TextMessage message = session.createTextMessage();
            message.setText(valueToPayload(value));
            return message;
        } catch (JsonProcessingException e) {
            log.warn("Could not convert to json.", e);
            throw new MessageConversionException("Could not convert to json.", e);
        }
    }

    private String valueToPayload(Object value) throws JsonProcessingException {
        return String.format(messageFormat, getClassName(value), mapper.writeValueAsString(value));
    }

    @Override
    public @NotNull Object fromMessage(@NotNull Message message) throws MessageConversionException {
        try {
            TextMessage textMessage = (TextMessage) message;
            String payload = textMessage.getText();

            return mapper.readValue(extractJsonFromPayload(payload), extractClassFromPayload(payload));
        } catch (JMSException | JsonProcessingException | ClassNotFoundException e) {
            log.warn("Could not convert from json.", e);
            throw new MessageConversionException("Could not convert from json.", e);
        }
    }

    @NotNull
    private Class<?> extractClassFromPayload(String payload) throws ClassNotFoundException {
        return getClass(payload.substring(0, payload.indexOf('|')));
    }

    @NotNull
    private String extractJsonFromPayload(String payload) {
        return payload.substring(payload.indexOf('|') + 1);
    }

    @NotNull
    private Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    private String getClassName(Object value) {
        return value.getClass().getName();
    }
}
