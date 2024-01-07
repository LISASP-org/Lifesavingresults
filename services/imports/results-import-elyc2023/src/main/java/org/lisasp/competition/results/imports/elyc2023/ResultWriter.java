package org.lisasp.competition.results.imports.elyc2023;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.lisasp.competition.results.imports.elyc2023.csv.HeaderColumnNameAndOrderMappingStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ResultWriter {

    public void write(Stream<Entry> data, Path path) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (OutputStream os = Files.newOutputStream(path); OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            new StatefulBeanToCsvBuilder<EMEntity>(writer).withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<>(
                    EMEntity.class)).withSeparator(';').build().write(data.filter(e -> e.isEntry() && e.isValid()).map(e -> e.toEntry()));
        }
    }

    public void writeInvalid(Stream<Entry> data, Path path) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (OutputStream os = Files.newOutputStream(path); OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            new StatefulBeanToCsvBuilder<EMEntity>(writer).withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<>(
                    EMEntity.class)).withSeparator(';').build().write(data.filter(e -> e.isEntry() && !e.isValid()).map(e -> e.toEntry()));
        }
    }

    public void writeOther(Stream<Entry> data, Path path) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (OutputStream os = Files.newOutputStream(path); OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            new StatefulBeanToCsvBuilder<EMEntity>(writer).withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<>(
                    EMEntity.class)).withSeparator(';').build().write(data.filter(e -> !e.isEntry()).map(e -> e.toEntry()));
        }
    }
}
