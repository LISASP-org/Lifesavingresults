package org.lisasp.competition.test.results.service.imports.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.results.api.imports.CsvEntry;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class CsvImportTests {

    @Test
    void test() throws IOException {
        Path path = Path.of("src", "test", "resources", "csv", "import-empty.csv");
        try (Reader reader = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)) {
            HeaderColumnNameTranslateMappingStrategy<CsvEntry> strategy = new HeaderColumnNameTranslateMappingStrategy<CsvEntry>();
            strategy.setType(CsvEntry.class);

            CsvToBean<CsvEntry> cb =
                    new CsvToBeanBuilder<CsvEntry>(reader).withMappingStrategy(strategy)
                                                          .withType(CsvEntry.class)
                                                          .withSeparator(';')
                                                          .build();
            CsvEntry[] rows = cb.parse().stream().toArray(CsvEntry[]::new);

            assertArrayEquals(new CsvEntry[0], rows);
        }
    }
}
