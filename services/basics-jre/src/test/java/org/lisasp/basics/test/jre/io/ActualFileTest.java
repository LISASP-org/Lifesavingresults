package org.lisasp.basics.test.jre.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.basics.jre.io.ActualFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActualFileTest {

    private ActualFile actualFile;
    private Path filename;

    private Path dirname;
    private Path fileInDirectory1;
    private Path fileInDirectory2;
    private Path fileInDirectory3;

    @BeforeEach
    void prepare() throws IOException {
        File tempFile = File.createTempFile("ActualFileTest-", "-test");
        filename = tempFile.toPath();
        Files.deleteIfExists(filename);

        File tmpDir = File.createTempFile("ActualFileTest-", "-directory");
        dirname = tmpDir.toPath();
        Files.deleteIfExists(dirname);
        Files.createDirectories(dirname);

        fileInDirectory1 = dirname.resolve("file1.txt");
        fileInDirectory2 = dirname.resolve("file2.dat");
        fileInDirectory3 = dirname.resolve("file3.bin");

        Files.write(fileInDirectory1, new byte[0]);
        Files.write(fileInDirectory2, new byte[0]);
        Files.write(fileInDirectory3, new byte[0]);

        actualFile = new ActualFile();
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(filename);

        deleteDirectory(dirname);
    }

    @Nested
    class AppendTest {
        @Test
        void singleExecution() throws IOException {
            actualFile.append(filename, (byte) 0x01);

            assertEquals(1, getFileSize());
        }

        @Test
        void multipleExecution() throws IOException {
            actualFile.append(filename, (byte) 0x04);
            actualFile.append(filename, (byte) 0x03);
            actualFile.append(filename, (byte) 0x02);
            actualFile.append(filename, (byte) 0x01);

            assertEquals(4, getFileSize());
        }

        @Test
        void nullTest() {
            assertThrows(NullPointerException.class, () -> actualFile.append(null, (byte) 0x00));
        }
    }

    @Nested
    class PutTest {
        @Test
        void singleExecution() throws IOException {
            actualFile.put(filename, (byte) 0x01);

            assertEquals(1, getFileSize());
        }

        @Test
        void multipleExecution() throws IOException {
            actualFile.put(filename, (byte) 0x04);
            actualFile.put(filename, (byte) 0x03);

            assertEquals(1, getFileSize());
        }

        @Test
        void nullTest() {
            assertThrows(NullPointerException.class, () -> actualFile.put(null, (byte) 0x00));
        }
    }

    @Nested
    class GetTest {
        @Test
        void read() throws IOException {
            Files.write(filename,
                    new byte[]{0x02, 0x03, 0x05, 0x06},
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE);

            byte[] actual = actualFile.get(filename);

            assertArrayEquals(new byte[]{0x02, 0x03, 0x05, 0x06}, actual);
        }

        @Test
        void readNonExisting() throws IOException {
            byte[] actual = actualFile.get(filename);
            assertArrayEquals(new byte[0], actual);
        }

        @Test
        void nullTest() {
            assertThrows(NullPointerException.class, () -> actualFile.get(null));
        }
    }

    @Nested
    class ExistsTest {
        @Test
        void exists() throws IOException {
            File tempFile = File.createTempFile("ActualFileExistsTest-", "-test");
            tempFile.deleteOnExit();
            Path pathToExistingFile = tempFile.toPath();

            boolean exists = actualFile.exists(pathToExistingFile);

            assertTrue(exists);
        }

        @Test
        void doesNotExist() throws IOException {
            boolean exists = actualFile.exists(filename);

            assertFalse(exists);
        }

        @Test
        void nullTest() {
            assertThrows(NullPointerException.class, () -> actualFile.exists(null));
        }
    }

    @Nested
    class CreateDirectoriesTest {
        @Test
        void exists() throws IOException {
            actualFile.createDirectories(dirname);

            assertTrue(Files.exists(dirname));
        }

        @Test
        void depth1() throws IOException {
            Path newDirectory = dirname.resolve("abc");

            actualFile.createDirectories(newDirectory);

            assertTrue(Files.exists(newDirectory));
        }

        @Test
        void depth2() throws IOException {
            Path newDirectory = dirname.resolve("abc").resolve("def");

            actualFile.createDirectories(newDirectory);

            assertTrue(Files.exists(newDirectory));
        }

        @Test
        void nullTest() {
            assertThrows(NullPointerException.class, () -> actualFile.createDirectories(null));
        }
    }

    @Nested
    class FindTest {
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2})
        void findDirectoriesTest(int maxDepth) throws IOException {
            List<Path> files = actualFile.find(dirname, maxDepth, (path, basicFileAttributes) -> basicFileAttributes.isDirectory()).toList();

            assertTrue(files.contains(dirname));
            assertEquals(1, files.size());
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        void findFilesTest(int maxDepth) throws IOException {
            List<Path> files = actualFile.find(dirname, maxDepth, (path, basicFileAttributes) -> basicFileAttributes.isRegularFile()).toList();

            assertTrue(files.contains(fileInDirectory1));
            assertTrue(files.contains(fileInDirectory2));
            assertTrue(files.contains(fileInDirectory3));
            assertEquals(3, files.size());
        }

        @Test
        void findFilesWithDepth0Test() throws IOException {
            List<Path> files = actualFile.find(dirname, 0, (path, basicFileAttributes) -> basicFileAttributes.isRegularFile()).toList();

            assertEquals(0, files.size());
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        void findTest(int maxDepth) throws IOException {
            List<Path> files = actualFile.find(dirname, maxDepth, (path, basicFileAttributes) -> true).toList();

            assertTrue(files.contains(dirname));
            assertTrue(files.contains(fileInDirectory1));
            assertTrue(files.contains(fileInDirectory2));
            assertTrue(files.contains(fileInDirectory3));
            assertEquals(4, files.size());
        }

        @Test
        void findWithDepth0Test() throws IOException {
            List<Path> files = actualFile.find(dirname, 0, (path, basicFileAttributes) -> true).toList();

            assertTrue(files.contains(dirname));
            assertEquals(1, files.size());
        }

        @Test
        void pathNullTest() {
            assertThrows(NullPointerException.class,
                    () -> actualFile.find(null, 0, (path, basicFileAttributes) -> true));
        }

        @Test
        void matcherNullTest() {
            assertThrows(NullPointerException.class,
                    () -> actualFile.find(dirname, 0, null));
        }
    }

    private void deleteDirectory(Path pathToBeDeleted) throws IOException {
        Files.walk(pathToBeDeleted)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    private long getFileSize() throws IOException {
        return Files.size(filename);
    }
}
