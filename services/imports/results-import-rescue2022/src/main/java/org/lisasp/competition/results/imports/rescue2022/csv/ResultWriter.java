package org.lisasp.competition.results.imports.rescue2022.csv;

import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.lisasp.competition.results.imports.rescue2022.RescueEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ResultWriter {

    public void write(Stream<RescueEntity> data, Path path) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (OutputStream os = Files.newOutputStream(path); OutputStreamWriter writer = new OutputStreamWriter(os,
                                                                                                               StandardCharsets.UTF_8)) {
            new StatefulBeanToCsvBuilder<RescueEntity>(writer).withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<>(
                    RescueEntity.class)).withSeparator(';').build().write(data);
        }
    }
}
