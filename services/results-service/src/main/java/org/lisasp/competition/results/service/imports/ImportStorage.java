package org.lisasp.competition.results.service.imports;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.lisasp.competition.base.api.exception.FileFormatException;
import org.lisasp.competition.results.api.imports.Competition;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
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
            saveToDisk(id, serialized);
            return StorageResult.Saved;
        } catch (IOException e) {
            throw new FileFormatException(e);
        }
    }

    private boolean isUnmodified(String id, String serialized) {
        try {
            String oldContent = contentsOfFileFor(id);
            return serialized.equals(oldContent);
        } catch (IOException ex) {
            log.debug("Could not read file for id {}", id);
            // If we cannot read the old file, the new content should be saved.
            return false;
        }
    }

    @NotNull
    private void saveToDisk(String id, String serialized) throws IOException {
        ensureDirectory();
        Files.writeString(filenameFor(id), serialized, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    private void ensureDirectory() throws IOException {
        Files.createDirectories(configuration.getStorageDirectory());
    }

    private String contentsOfFileFor(String id) throws IOException {
        return Files.readString(filenameFor(id), StandardCharsets.UTF_8);
    }

    @NotNull
    private Path filenameFor(String id) {
        return configuration.getStorageDirectory().resolve(id + ".json");
    }
}
