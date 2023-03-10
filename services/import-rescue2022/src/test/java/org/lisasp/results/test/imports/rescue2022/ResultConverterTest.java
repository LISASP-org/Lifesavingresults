package org.lisasp.results.test.imports.rescue2022;

import org.junit.jupiter.api.Test;
import org.lisasp.results.imports.rescue2022.result.ResultConverter;
import org.lisasp.results.imports.rescue2022.result.model.ResultFile;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResultConverterTest {
    @Test
    void test() throws IOException {
        ResultConverter converter = new ResultConverter();

        ResultFile actual = converter.load(Path.of("src", "test", "resources", "results", "001.JSON"));

        assertNotNull(actual);

        assertEquals("001.JSON", actual.getJsonfilename());
        assertEquals("1", actual.getCounter());
        assertEquals("2", actual.getTipologia());
        assertEquals("3", actual.getVisSoc());
        assertEquals("4", actual.getVisNaz());
        assertEquals("5", actual.getVisCat());
        assertEquals("6", actual.getVisPunti());
        assertEquals("7", actual.getScorpora());
        assertEquals("001.pdf", actual.getPdf());
        assertEquals("111", actual.getStatoBatt());
    }
}
