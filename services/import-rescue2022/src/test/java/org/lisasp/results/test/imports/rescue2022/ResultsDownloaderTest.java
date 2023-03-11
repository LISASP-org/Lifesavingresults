package org.lisasp.results.test.imports.rescue2022;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.lisasp.basics.jre.io.ActualFile;
import org.lisasp.basics.jre.io.FileFacade;
import org.lisasp.results.imports.rescue2022.Downloader;
import org.lisasp.results.imports.rescue2022.ResultsDownloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultsDownloaderTest {

    private Downloader downloader;

    private FileFacade files;

    @BeforeEach
    void prepare() {
        downloader = new ResourceDownloader();
        // files = mock(FileFacade.class);
        files = new ActualFile();
    }

    private boolean isEmpty(Path directory) throws IOException {
        return Files.list(directory).count() == 0;
    }

    @Test
    void emptyList(@TempDir Path directory) throws IOException {
        new ResultsDownloader(downloader, files, directory).download("competition", "Empty");

        assertTrue(isEmpty(directory));
    }

    @Test
    void categoryIsEmptyList(@TempDir Path directory) throws IOException {
        new ResultsDownloader(downloader, files, directory).download("competition", "Category");

        assertTrue(isEmpty(directory));
    }

    @Test
    void orarioIsEmptyList(@TempDir Path directory) throws IOException {
        new ResultsDownloader(downloader, files, directory).download("competition", "Orario");

        assertTrue(isEmpty(directory));
    }

    @Test
    void oneEntry(@TempDir Path directory) throws IOException {
        new ResultsDownloader(downloader, files, directory).download("competition", "OneEntry");

        Path targetDirectory = directory.resolve(Path.of("data", "downloads", "competition", "OneEntry"));
        Path json001 = targetDirectory.resolve("001.JSON");
        assertTrue(Files.exists(targetDirectory));
        assertTrue(Files.exists(json001));
        assertArrayEquals(new byte[]{1}, Files.readAllBytes(json001));

        assertArrayEquals(new String[]{"001.JSON"}, Files.list(targetDirectory).map(p -> p.getFileName().toString()).toArray(String[]::new));
    }

    @Test
    void twoEntries(@TempDir Path directory) throws IOException {
        new ResultsDownloader(downloader, files, directory).download("competition", "TwoEntries");

        Path targetDirectory = directory.resolve(Path.of("data", "downloads", "competition", "TwoEntries"));
        Path json001 = targetDirectory.resolve("001.JSON");
        Path json002 = targetDirectory.resolve("002.JSON");
        assertTrue(Files.exists(targetDirectory));
        assertTrue(Files.exists(json001));
        assertTrue(Files.exists(json002));
        assertArrayEquals(new byte[]{2}, Files.readAllBytes(json001));
        assertArrayEquals(new byte[]{3}, Files.readAllBytes(json002));

        assertArrayEquals(new String[]{"001.JSON", "002.JSON"}, Files.list(targetDirectory).map(p -> p.getFileName().toString()).sorted().toArray(String[]::new));
    }

    @Test
    void dontWriteIfExists(@TempDir Path directory) throws IOException {
        Path targetDirectory = directory.resolve(Path.of("data", "downloads", "competition", "TwoEntries"));
        Path json001 = targetDirectory.resolve("001.JSON");
        Path json002 = targetDirectory.resolve("002.JSON");
        files.createDirectories(targetDirectory);
        files.put(json002, new byte[]{10});

        new ResultsDownloader(downloader, files, directory).download("competition", "TwoEntries");

        assertTrue(Files.exists(targetDirectory));
        assertTrue(Files.exists(json001));
        assertTrue(Files.exists(json002));
        assertArrayEquals(new byte[]{2}, Files.readAllBytes(json001));
        assertArrayEquals(new byte[]{10}, Files.readAllBytes(json002));
    }

    private static class ResourceDownloader implements Downloader {

        @Override
        public byte[] download(String type, String filename) {
            try {
                if (type.equals("Empty") && filename.equals("Contatori.json")) {
                    return ResultsDownloaderTest.class.getResourceAsStream("/category/Empty.json").readAllBytes();
                }
                if (type.equals("Orario") && filename.equals("Contatori.json")) {
                    return ResultsDownloaderTest.class.getResourceAsStream("/category/Orario.json").readAllBytes();
                }
                if (type.equals("Category") && filename.equals("Contatori.json")) {
                    return ResultsDownloaderTest.class.getResourceAsStream("/category/Category.json").readAllBytes();
                }
                if (type.equals("OneEntry") && filename.equals("Contatori.json")) {
                    return ResultsDownloaderTest.class.getResourceAsStream("/category/OneEntry.json").readAllBytes();
                }
                if (type.equals("TwoEntries") && filename.equals("Contatori.json")) {
                    return ResultsDownloaderTest.class.getResourceAsStream("/category/TwoEntries.json").readAllBytes();
                }
                if (type.equals("OneEntry") && filename.equals("001.JSON")) {
                    return new byte[]{1};
                }
                if (type.equals("TwoEntries") && filename.equals("001.JSON")) {
                    return new byte[]{2};
                }
                if (type.equals("TwoEntries") && filename.equals("002.JSON")) {
                    return new byte[]{3};
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new IllegalArgumentException(String.format("%s - %s", type, filename));
        }
    }
}
