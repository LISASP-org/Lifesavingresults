package org.lisasp.competition.results.imports.em2023;

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

    private final String baseurl;

    private URI toURI(String filename) throws URISyntaxException {
        return new URI(String.format("%s/%s", baseurl, filename));
    }

    @Override
    public byte[] download(String filename) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(toURI(filename))
                                             .version(HttpClient.Version.HTTP_2)
                                             .GET()
                                             .build();

            HttpResponse<byte[]> response = HttpClient.newBuilder()
                                                      .build()
                                                      .send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            }
            log.info("Status code: {}", response.statusCode());
            return null;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
