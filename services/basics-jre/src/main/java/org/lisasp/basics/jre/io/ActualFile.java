package org.lisasp.basics.jre.io;

import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * Uses the Files class to provide access to the filesystem.
 */
public class ActualFile implements FileFacade {

    @Override
    public void append(Path file, byte... b) throws IOException {
        Files.createDirectories(file.getParent());
        Files.write(file,
                b,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE);
    }

    @Override
    public void put(Path file, byte... b) throws IOException {
        Files.createDirectories(file.getParent());
        Files.write(file,
                b,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE);
    }

    @Override
    public byte[] get(Path file) throws IOException {
        try {
            return Files.readAllBytes(file);
        } catch (NoSuchFileException ex) {
            // if the file is not found, nothing has been saved yet.
            return new byte[0];
        }
    }

    @Override
    public boolean exists(Path path) {
        return Files.exists(path);
    }

    @Override
    public void createDirectories(Path path) throws IOException {
        Files.createDirectories(path);
    }

    public Stream<Path> find(Path basePath, int maxDepth, @NonNull BiPredicate<Path, BasicFileAttributes> matcher) throws IOException {
        return Files.find(basePath, maxDepth, matcher);
    }
}
