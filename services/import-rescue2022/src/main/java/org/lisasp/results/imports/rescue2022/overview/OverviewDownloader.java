package org.lisasp.results.imports.rescue2022.overview;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.results.imports.rescue2022.Downloader;
import org.lisasp.results.imports.rescue2022.overview.model.Overview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class OverviewDownloader {

    private static final Set<String> excludedFiles = Stream.of("orario.JSON", "categorie.JSON").collect(Collectors.toSet());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Downloader downloader;

    public List<String> getFilenames(String name) throws IOException {
        byte[] content = downloader.download(name, "Contatori.json");
        Overview overview = objectMapper.readValue(content, Overview.class);
        return Arrays.stream(overview.getContatori()).map(e -> e.getNomefile()).filter(filename -> !excludedFiles.contains(filename)).toList();
    }

    public void download(String competition, String type) throws IOException {
        getFilenames(type).forEach(f -> {
            downloadFile(competition, type, f);
        });
    }

    private void downloadFile(String competition, String type, String f) {
        try {
            Path directory = Path.of(".", "data", "downloads", competition, type);
            Files.createDirectories(directory);
            Path localFilename = directory.resolve(f);
            if (Files.exists(localFilename)) {
                log.info("Skipping file '{}'.", f);
            } else {
                log.info("Downloading file '{}'...", f);
                byte[] content = downloader.download(type, f);
                Files.write(localFilename, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
