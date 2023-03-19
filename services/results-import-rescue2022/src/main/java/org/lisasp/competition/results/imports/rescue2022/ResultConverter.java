package org.lisasp.competition.results.imports.rescue2022;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.results.imports.rescue2022.model.result.ResultFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ResultConverter {

    private static final ReplacePattern[] NO_COLON_BEFORE = createReplacePatterns(Stream.of("},", "}]"));

    private final ObjectMapper mapper = new ObjectMapper();

    private final String defaultAgegroup;

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

    public Event convert(Path file) throws IOException {
        ResultFile resultFile = load(file);
        // Swim-Offs are not included in the summary
        if (!isSummary(resultFile) && !isSwimOff(resultFile)) {
            return null;
        }
        return extractEvent(resultFile);
    }

    private boolean isSummary(ResultFile resultFile) {
        return resultFile.getDocument().getCode().equals("SUM");
    }

    private boolean isSwimOff(ResultFile resultFile) {
        return resultFile.getJsonFilename().contains("CLAS") && resultFile.getRound().getCode() == 4;
    }

    private Event extractEvent(ResultFile resultFile) {
        return new EventCreator(defaultAgegroup).parse(resultFile);
    }

    private record ReplacePattern(String search, String replace) {
        private String replace(String text) {
            return text.replace(search, replace);
        }
    }
}
