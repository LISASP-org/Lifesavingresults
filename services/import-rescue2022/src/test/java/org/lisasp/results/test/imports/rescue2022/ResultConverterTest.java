package org.lisasp.results.test.imports.rescue2022;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.lisasp.results.imports.rescue2022.result.ResultConverter;
import org.lisasp.results.imports.rescue2022.result.model.ResultFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResultConverterTest {
    @Test
    void simpleFile() throws IOException {
        ResultConverter converter = new ResultConverter();

        ResultFile actual = converter.load(Path.of("src", "test", "resources", "results", "001.JSON"));

        assertNotNull(actual);

        assertEquals("001.JSON", actual.getJsonFilename());
        assertEquals("1", actual.getCounter());
        assertEquals("2", actual.getTipologia());
        assertEquals("3", actual.getVisSoc());
        assertEquals("4", actual.getVisNaz());
        assertEquals("5", actual.getVisCat());
        assertEquals("6", actual.getVisPunti());
        assertEquals("7", actual.getScorpora());
        assertEquals("001.pdf", actual.getPdfFilename());
        assertEquals("111", actual.getStatoBatt());
    }

    @ParameterizedTest
    @MethodSource(value = "findFiles")
    void downloadedFiles(Path filename) throws IOException {
        ResultConverter converter = new ResultConverter();
        ResultFile actual = converter.load(filename);

        assertNotNull(actual);
    }

    static List<Path> findFiles() throws IOException {
        List<Path> filenames = new ArrayList<>();
        if (Files.exists(Path.of("data", "downloads", "Rescue 2022"))) {
            filenames.addAll(Files.list(Path.of("data", "downloads", "Rescue 2022", "Master")).filter(onlyNormalResultFiles()).toList());
            filenames.addAll(Files.list(Path.of("data", "downloads", "Rescue 2022", "Youth")).filter(onlyNormalResultFiles()).toList());
            filenames.addAll(Files.list(Path.of("data", "downloads", "Rescue 2022", "Open")).filter(onlyNormalResultFiles()).toList());
        }
        if (Files.exists(Path.of("data", "downloads", "RescueCup 2022"))) {
            filenames.addAll(Files.list(Path.of("data", "downloads", "RescueCup 2022", "RescueCup")).filter(onlyNormalResultFiles()).toList());
        }
        return filenames;
    }

    @NotNull
    private static Predicate<Path> onlyNormalResultFiles() {
        return f -> {
            String filename = f.toString();
            return !filename.contains("ScheduleByDate_") && !filename.contains("ScheduleByEvent_") && !filename.endsWith("NUMZMD.JSON") && !filename.endsWith("NUS1SOC.JSON");
        };
    }
}
