package org.lisasp.results.imports.rescue2022;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.basics.jre.io.FileFacade;
import org.lisasp.results.imports.rescue2022.model.overview.Overview;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class ResultsDownloader {

    private static final Set<String> excludedFiles = Stream.of("orario.JSON", "categorie.JSON").collect(Collectors.toSet());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Downloader downloader;

    private final FileFacade files;

    private final Path basedir;

    private List<String> getFilenames(String name) throws IOException {
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
            Path directory = basedir.resolve(Path.of("data", "downloads", competition, type));
            files.createDirectories(directory);
            Path localFilename = directory.resolve(f);
            if (files.exists(localFilename)) {
                log.info("Skipping file '{}'.", f);
            } else {
                log.info("Downloading file '{}'...", f);
                byte[] content = downloader.download(type, f);
                files.put(localFilename, content);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
