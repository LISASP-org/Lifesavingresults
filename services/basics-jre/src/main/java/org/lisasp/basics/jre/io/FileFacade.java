package org.lisasp.basics.jre.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * FileFacade provides an instance based approach to the filesystem.
 */
public interface FileFacade {
    /**
     * Writes binary data to a file. Overwriting existing data.
     *
     * @param file  Path to file
     * @param bytes Data to be written
     * @throws IOException if an I/O error occurs
     */
    void put(Path file, byte... bytes) throws IOException;

    /**
     * Appends binary data to a file. Creates a new File if necessary.
     *
     * @param file  Path to the file
     * @param bytes Data to be written
     * @throws IOException if an I/O error occurs
     */
    void append(Path file, byte... bytes) throws IOException;

    /**
     * Reads binary data from a file.
     *
     * @param file Path to the file
     * @return Data of the file
     * @throws IOException if an I/O error occurs
     */
    byte[] get(Path file) throws IOException;

    /**
     * Checks the existence of a file or directory.
     *
     * @param path Path to the file or directory
     * @return true if the file exists, false otherwise.
     */
    boolean exists(Path path);

    /**
     * Creates directories for a given path.
     *
     * @param path Path of the directory
     * @throws IOException if an I/O error occurs
     */
    void createDirectories(Path path) throws IOException;

    /**
     * Search for files or directories matching given criteria.
     *
     * @param basePath Path the to starting directory.
     * @param maxDepth Maximum recursion depth.
     * @param matcher  Matching criteria.
     * @return a stream of all matching paths.
     * @throws IOException if an I/O error occurs
     */
    Stream<Path> find(Path basePath, int maxDepth, BiPredicate<Path, BasicFileAttributes> matcher) throws IOException;
}
