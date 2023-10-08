package org.lisasp.competition;

import lombok.Getter;
import lombok.Setter;
import org.lisasp.competition.results.service.imports.ImportConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
@Getter
@Setter
public class ConfigurationValues implements ImportConfiguration {

    @Value("${results.imports.directory:./imports}")
    private Path storageDirectory;
}
