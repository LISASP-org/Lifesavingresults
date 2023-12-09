package org.lisasp.competition.results.imports.rescue2022;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.results.imports.rescue2022.csv.ResultWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class ResultsConverter {

    private final Pattern pattern = Pattern.compile("^NU\\w{3}\\d{3}\\w{4}\\d{2} \\d{3}\\.JSON$");

    private final Path basedir;

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public void convert(String name, String type) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        ResultConverter resultConverter = new ResultConverter(type);

        Path directory = basedir.resolve(Path.of("data", "downloads", name, type));

        List<Event> events;
        try (Stream<Path> files = Files.list(directory)) {
            events = files.map(path -> convert(resultConverter, path)).filter(Objects::nonNull).toList();
        }

        Competition competition = Competition.builder().name(name).events(events.toArray(Event[]::new)).build();
        Files.createDirectories(basedir.resolve(Path.of("data", "results")));
        Files.write(basedir.resolve(Path.of("data", "results", String.format("%s-%s.json", name, type))), mapper.writeValueAsBytes(competition));

        new ResultWriter().write(toCSV(competition), Path.of("data", "results", String.format("%s-%s.csv", name, type)));
    }

    private Stream<RescueEntity> toCSV(Competition competition) {
        List<RescueEntity> entities = new ArrayList<>();
        for (Event event : competition.events()) {
            entities.addAll(entitiesOf(event));
        }
        return entities.stream();
    }

    private List<RescueEntity> entitiesOf(Event event) {
        List<RescueEntity> entities = new ArrayList<>();
        for (Entry entry : event.entries()) {
            entities.add(entityOf(entry, event));
        }
        return entities;
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private RescueEntity entityOf(Entry entry, Event event) {
        return new RescueEntity("", entry.name(), yearOfBirth(entry), entry.club(), "", toTime(entry.timeInMillis()), event.agegroup(), event.gender().toString(), event.discipline(),
                                formatDate(event.date()), "");
    }

    @NotNull
    private static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return formatter.format(date);
    }

    private String toTime(int timeInMillis) {
        int minutes = timeInMillis / 1000 / 60;
        int seconds = (timeInMillis / 1000) % 60;
        int hundrets = (timeInMillis / 10) % 100;

        return String.format("%d:%02d,%02d", minutes, seconds, hundrets);
    }

    private String yearOfBirth(Entry entry) {
        if (entry.swimmer() == null || entry.swimmer().length != 1) {
            return "";
        }
        return ""+ entry.swimmer()[0].yearOfBirth();
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
