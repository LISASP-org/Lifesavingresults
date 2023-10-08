package org.lisasp.competition.test.results.service.imports.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.results.api.imports.CsvEntry;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class CsvImportTests {

    @Test
    void test() throws IOException {
        Path path = Path.of("src","test", "resources", "csv", "import-empty.csv");
        try (Reader reader = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)) {
            CsvToBean<CsvEntry> cb = new CsvToBeanBuilder<CsvEntry>(reader).withType(CsvEntry.class).withSeparator(';').build();
            CsvEntry[] rows = cb.parse().stream().toArray(CsvEntry[]::new);
        }
    }
}
