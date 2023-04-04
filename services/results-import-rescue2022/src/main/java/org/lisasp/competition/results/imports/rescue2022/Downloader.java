package org.lisasp.competition.results.imports.rescue2022;

public interface Downloader {
    byte[] download(String type, String filename);
}
