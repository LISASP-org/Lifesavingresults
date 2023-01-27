package org.lisasp.results.imports.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.lisasp.results.imports.api.Competition;
import org.lisasp.results.imports.jauswertung.FileFormatException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImportStorage {

    private final ImportServiceConfiguration config;

    private final ObjectMapper mapper = new ObjectMapper();

    void put(String id, Competition competition) throws FileFormatException {
        try {
            ensureDirectory();
            Files.write(filenameFor(id), mapper.writeValueAsString(competition).getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new FileFormatException(e);
        }
    }

    private void ensureDirectory() throws IOException {
        Files.createDirectories(Path.of(config.getStorageDirectory()));
    }

    Optional<Competition> get(String id) throws FileFormatException {
        try {
            return Optional.of(deserialize(fileFor(id)));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    private String fileFor(String id) throws IOException {
        return Files.readString(filenameFor(id), StandardCharsets.UTF_8);
    }

    @NotNull
    private Path filenameFor(String id) {
        return Path.of(config.getStorageDirectory(), id + ".json");
    }

    private Competition deserialize(String json) throws FileFormatException {
        try {
            return mapper.readValue(json, Competition.class);
        } catch (JsonProcessingException ex) {
            throw new FileFormatException(ex);
        }
    }

}
