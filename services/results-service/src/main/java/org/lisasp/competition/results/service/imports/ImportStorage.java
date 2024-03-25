package org.lisasp.competition.results.service.imports;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.lisasp.competition.base.api.exception.FileFormatException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.base.api.exception.StorageException;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.Event;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Arrays.stream;

@Slf4j
@RequiredArgsConstructor
public class ImportStorage {

    private final ImportConfiguration configuration;

    private final ObjectMapper mapper = new ObjectMapper();

    StorageResult put(String id, int index, Competition competition) throws FileFormatException {
        try {
            String serialized = mapper.writeValueAsString(competition);
            if (isUnmodified(id, index, serialized)) {
                return StorageResult.Unchanged;
            }
            saveToDisk(id, index, serialized);
            return StorageResult.Saved;
        } catch (IOException e) {
            throw new FileFormatException(e);
        }
    }

    private boolean isUnmodified(String id, int index, String serialized) {
        try {
            String oldContent = contentsOfFileFor(id, index);
            return serialized.equals(oldContent);
        } catch (IOException ex) {
            log.debug("Could not read file for id {}", id);
            // If we cannot read the old file, the new content should be saved.
            return false;
        }
    }

    private void saveToDisk(String id, int index, String serialized) throws IOException {
        ensureDirectory(id);
        Files.writeString(filenameFor(id, index),
                          serialized,
                          StandardOpenOption.WRITE,
                          StandardOpenOption.TRUNCATE_EXISTING,
                          StandardOpenOption.CREATE);
    }

    private void ensureDirectory(String id) throws IOException {
        Files.createDirectories(configuration.getStorageDirectory().resolve(id));
    }

    private String contentsOfFileFor(String id, int index) throws IOException {
        return Files.readString(filenameFor(id, index), StandardCharsets.UTF_8);
    }

    @NotNull
    private Path filenameFor(String id, int index) {
        return configuration.getStorageDirectory().resolve(Path.of(id, format("%d.json", index)));
    }

    public Competition get(String id) throws NotFoundException, StorageException {
        if (!Files.exists(configuration.getStorageDirectory().resolve(id))) {
            throw new NotFoundException("Storage", id);
        }
        try (Stream<Path> files = Files.list(configuration.getStorageDirectory().resolve(id))) {
            List<Competition> competitions = new ArrayList<>();
            Path[] paths = files
                    .filter(Files::isRegularFile).toArray(Path[]::new);
            if (paths.length == 0) {
                throw new NotFoundException("Storage", id);
            }
            for (Path path : paths) {
                competitions.add(readFromDisk(path));
            }
            return merge(competitions.toArray(Competition[]::new));
        } catch (FileNotFoundException e) {
            throw new NotFoundException("Storage", id);
        } catch (IOException e) {
            throw new StorageException(id, e);
        }

    }

    private Competition merge(Competition[] competitions) {
        Competition first = competitions[0];
        Event[] events = stream(competitions).map(Competition::events).flatMap(Stream::of).toArray(Event[]::new);
        return new Competition(first.name(),
                               first.acronym(),
                               first.from(),
                               first.till(),
                               events);
    }

    private Competition readFromDisk(Path path) throws IOException {
        return mapper.readValue(Files.readString(path, StandardCharsets.UTF_8), Competition.class);
    }
}
