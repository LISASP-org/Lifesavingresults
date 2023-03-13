package org.lisasp.competition.results.imports.rescue2022;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class HttpDownloader implements Downloader {

    private final String format;

    private final URI toURI(String type, String filename) throws URISyntaxException {
        return new URI(String.format(format, type, filename).replace(" ", "%20"));
    }

    @Override
    public byte[] download(String type, String filename) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(toURI(type, filename))
                    .version(HttpClient.Version.HTTP_2)
                    .GET()
                    .build();

            HttpResponse<byte[]> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return new String(response.body(), "windows-1252").getBytes(StandardCharsets.UTF_8);
            }
            log.info("Statuscode: {}", response.statusCode());
            return null;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
