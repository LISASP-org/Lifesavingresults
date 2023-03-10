package org.lisasp.results.test.imports.rescue2022;

import org.junit.jupiter.api.Test;
import org.lisasp.results.imports.rescue2022.Downloader;
import org.lisasp.results.imports.rescue2022.overview.OverviewDownloader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class OverviewDownloaderTest {

    @Test
    void emptyList() throws IOException {
        List<String> actual = new OverviewDownloader(new ResourceDownloader()).getFilenames("Empty");

        assertArrayEquals(new String[0], actual.toArray(String[]::new));
    }

    @Test
    void categoryIsEmptyList() throws IOException {
        List<String> actual = new OverviewDownloader(new ResourceDownloader()).getFilenames("Category");

        assertArrayEquals(new String[0], actual.toArray(String[]::new));
    }

    @Test
    void orarioIsEmptyList() throws IOException {
        List<String> actual = new OverviewDownloader(new ResourceDownloader()).getFilenames("Orario");

        assertArrayEquals(new String[0], actual.toArray(String[]::new));
    }

    @Test
    void oneEntry() throws IOException {
        List<String> actual = new OverviewDownloader(new ResourceDownloader()).getFilenames("OneEntry");

        assertArrayEquals(new String[]{"001.JSON"}, actual.toArray(String[]::new));
    }

    @Test
    void twoEntries() throws IOException {
        List<String> actual = new OverviewDownloader(new ResourceDownloader()).getFilenames("TwoEntries");

        assertArrayEquals(new String[]{"001.JSON", "002.JSON"}, actual.toArray(String[]::new));
    }

    private static class ResourceDownloader implements Downloader {

        @Override
        public byte[] download(String type, String filename) {
            try {
                if (type.equals("Empty") && filename.equals("Contatori.json")) {
                    return OverviewDownloaderTest.class.getResourceAsStream("/category/Empty.json").readAllBytes();
                }
                if (type.equals("Orario") && filename.equals("Contatori.json")) {
                    return OverviewDownloaderTest.class.getResourceAsStream("/category/Orario.json").readAllBytes();
                }
                if (type.equals("Category") && filename.equals("Contatori.json")) {
                    return OverviewDownloaderTest.class.getResourceAsStream("/category/Category.json").readAllBytes();
                }
                if (type.equals("OneEntry") && filename.equals("Contatori.json")) {
                    return OverviewDownloaderTest.class.getResourceAsStream("/category/OneEntry.json").readAllBytes();
                }
                if (type.equals("TwoEntries") && filename.equals("Contatori.json")) {
                    return OverviewDownloaderTest.class.getResourceAsStream("/category/TwoEntries.json").readAllBytes();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new IllegalArgumentException(String.format("%s - %s", type, filename));
        }
    }
}
