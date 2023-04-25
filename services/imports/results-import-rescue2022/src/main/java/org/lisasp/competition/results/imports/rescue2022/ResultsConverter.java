package org.lisasp.competition.results.imports.rescue2022;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class ResultsConverter {

    private final Pattern pattern = Pattern.compile("^NU\\w{3}\\d{3}\\w{4}\\d{2} \\d{3}\\.JSON$");

    private final Path basedir;

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public void convert(String name, String type) throws IOException {
        ResultConverter resultConverter = new ResultConverter(type);

        Path directory = basedir.resolve(Path.of("data", "downloads", name, type));

        List<Event> events = Files.list(directory).map(path -> convert(resultConverter, path)).filter(e -> e != null).toList();

        Competition competition = Competition.builder().name(name).events(events.toArray(Event[]::new)).build();
        Files.createDirectories(basedir.resolve(Path.of("data", "results")));
        Files.write(basedir.resolve(Path.of("data", "results", String.format("%s-%s.json", name, type))), mapper.writeValueAsBytes(competition));
    }

    private Event convert(ResultConverter resultConverter, Path file) {
        try {
            String filename = file.getFileName().toString();
            Matcher matcher = pattern.matcher(filename);
            if (!matcher.matches()) {
                return null;
            }
            log.info("Converting '{}'", filename);
            return resultConverter.convert(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
