package org.lisasp.results;

import lombok.Getter;
import lombok.Setter;
import org.lisasp.results.imports.core.ImportConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ConfigurationValues implements ImportConfiguration {

    @Value("${results.imports.directory:./imports}")
    private String storageDirectory;
}
