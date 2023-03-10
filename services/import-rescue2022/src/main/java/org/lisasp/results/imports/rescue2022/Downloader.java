package org.lisasp.results.imports.rescue2022;

import java.io.InputStream;

public interface Downloader {
    byte[] download(String type, String filename);
}
