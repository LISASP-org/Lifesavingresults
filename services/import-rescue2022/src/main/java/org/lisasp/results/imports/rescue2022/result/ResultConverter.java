package org.lisasp.results.imports.rescue2022.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lisasp.results.imports.rescue2022.result.model.ResultFile;

import javax.print.attribute.standard.NumberOfInterveningJobs;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResultConverter {

    private final ObjectMapper mapper = new ObjectMapper();
    public ResultFile load(Path filename) throws IOException {
        return mapper.readValue(Files.readString(filename), ResultFile.class);
    }
}
