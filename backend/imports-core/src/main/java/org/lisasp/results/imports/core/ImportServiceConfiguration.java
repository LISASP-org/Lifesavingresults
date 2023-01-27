package org.lisasp.results.imports.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ImportServiceConfiguration {

    @Value("${results.imports.directory:./imports}")
    private String storageDirectory;
}
