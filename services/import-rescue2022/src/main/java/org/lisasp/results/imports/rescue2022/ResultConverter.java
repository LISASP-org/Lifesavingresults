package org.lisasp.results.imports.rescue2022;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.lisasp.results.imports.rescue2022.model.result.ResultFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ResultConverter {

    private static final ReplacePattern[] NO_COLON_BEFORE = createReplacePatterns(Stream.of("},", "}]"));

    private final ObjectMapper mapper = new ObjectMapper();

    public ResultFile load(Path filename) throws IOException {
        String content = Files.readString(filename);
        for (ReplacePattern pattern : NO_COLON_BEFORE) {
            content = pattern.replace(content);
        }
        return mapper.readValue(content, ResultFile.class);
    }

    private static ReplacePattern[] createReplacePatterns(Stream<String> templates) {
        List<ReplacePattern> patterns = new ArrayList<>();
        templates.forEach(t -> {
            patterns.add(new ReplacePattern(String.format(",\r\n%s\r\n", t), String.format("\r\n%s\r\n", t)));
            patterns.add(new ReplacePattern(String.format(",\n%s\n", t), String.format("\n%s\n", t)));
        });
        return patterns.toArray(ReplacePattern[]::new);
    }

    @Value
    private static class ReplacePattern {
        private final String search;
        private final String replace;

        private String replace(String text) {
            return text.replace(search, replace);
        }
    }
}
