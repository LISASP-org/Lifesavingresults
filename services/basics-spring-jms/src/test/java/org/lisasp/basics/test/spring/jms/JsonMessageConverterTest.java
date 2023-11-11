package org.lisasp.basics.test.spring.jms;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.spring.jms.JsonMessageConverter;
import org.lisasp.basics.test.spring.jms.data.TestData;
import org.lisasp.basics.test.spring.jms.data.TestRecord;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class JsonMessageConverterTest {

    private final String content1 = TestData.class.getCanonicalName() + "|{\"text\":\"abc\",\"value\":123}";
    private final String content2 = TestRecord.class.getCanonicalName() + "|{\"text\":\"def\",\"value\":456,\"date\":[2021,11,19]}";

    private final TestData testData1 = new TestData("abc", 123);
    private final TestRecord testData2 = new TestRecord("def", 456, LocalDate.of(2021, 11, 19));

    @Test
    void objectToStringTest() throws JMSException {
        Session session = mock(Session.class);
        TextMessage message = mock(TextMessage.class);
        given(session.createTextMessage()).willReturn(message);

        JsonMessageConverter converter = new JsonMessageConverter();
        Message msg = converter.toMessage(testData1, session);

        verify(message, times(1)).setText(content1);
        assertTrue(msg instanceof TextMessage);
        assertEquals(message.getText(), ((TextMessage) msg).getText());
    }

    @Test
    void stringToObjectTest() throws JMSException {
        Session session = mock(Session.class);
        TextMessage message = mock(TextMessage.class);
        given(session.createTextMessage()).willReturn(message);
        given(message.getText()).willReturn(content1);

        JsonMessageConverter converter = new JsonMessageConverter();
        Object actual = converter.fromMessage(message);

        assertEquals(testData1, actual);
    }

    @Test
    void recordsToStringTest() throws JMSException {
        Session session = mock(Session.class);
        TextMessage message = mock(TextMessage.class);
        given(session.createTextMessage()).willReturn(message);

        JsonMessageConverter converter = new JsonMessageConverter();
        Message msg = converter.toMessage(testData2, session);

        verify(message, times(1)).setText(content2);
        assertTrue(msg instanceof TextMessage);
        assertEquals(message.getText(), ((TextMessage) msg).getText());
    }

    @Test
    void stringToRecordTest() throws JMSException {
        Session session = mock(Session.class);
        TextMessage message = mock(TextMessage.class);
        given(session.createTextMessage()).willReturn(message);
        given(message.getText()).willReturn(content2);

        JsonMessageConverter converter = new JsonMessageConverter();
        Object actual = converter.fromMessage(message);

        assertEquals(testData2, actual);
    }
}
