package org.lisasp.competition.results.service.imports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.base.api.exception.FileFormatException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@RequiredArgsConstructor
public class ImportStorage {

    private final ImportConfiguration configuration;

    private final ObjectMapper mapper = new ObjectMapper();

    StorageResult put(String id, Competition competition) throws FileFormatException {
        try {
            String serialized = mapper.writeValueAsString(competition);
            if (isUnmodified(id, serialized)) {
                return StorageResult.Unchanged;
            }
            return saveToDisk(id, serialized);
        } catch (IOException e) {
            throw new FileFormatException(e);
        }
    }

    private boolean isUnmodified(String id, String serialized) {
        try {
            String oldContent = contentsOfFileFor(id);
            return serialized.equals(oldContent);
        } catch (IOException ex) {
            // If we cannot read the old file, the new content should be saved.
            return false;
        }
    }

    @NotNull
    private StorageResult saveToDisk(String id, String serialized) throws IOException {
        ensureDirectory();
        Files.write(filenameFor(id), serialized.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        return StorageResult.Saved;
    }

    private void ensureDirectory() throws IOException {
        Files.createDirectories(Path.of(configuration.getStorageDirectory()));
    }

    private Optional<Competition> get(String id) throws FileFormatException {
        try {
            return Optional.of(deserialize(contentsOfFileFor(id)));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    private String contentsOfFileFor(String id) throws IOException {
        return Files.readString(filenameFor(id), StandardCharsets.UTF_8);
    }

    @NotNull
    private Path filenameFor(String id) {
        return Path.of(configuration.getStorageDirectory(), id + ".json");
    }

    private Competition deserialize(String json) throws FileFormatException {
        try {
            return mapper.readValue(json, Competition.class);
        } catch (JsonProcessingException ex) {
            throw new FileFormatException(ex);
        }
    }

}
