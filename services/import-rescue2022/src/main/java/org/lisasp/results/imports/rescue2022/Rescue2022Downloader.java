package org.lisasp.results.imports.rescue2022;

import lombok.extern.slf4j.Slf4j;
import org.lisasp.basics.jre.io.ActualFile;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class Rescue2022Downloader {

    public static final Path DIRECTORY = Path.of(".");

    public static void main(String[] args) throws IOException {
        process("Rescue 2022", "Open");
        process("Rescue 2022", "Youth");
        process("Rescue 2022", "Master");
        process("RescueCup 2022", "RescueCup");
    }

    private static void process(String competition, String type) throws IOException {
        log.info("Processing {} {}", competition, type);
        HttpDownloader downloader = new HttpDownloader("https://lifesavingwc2022.microplustimingservices.com/export/LWC2022_%s/NU/%s");
        new ResultsDownloader(downloader, new ActualFile(), DIRECTORY).download(competition, type);
        new ResultsConverter(DIRECTORY).convert(competition, type);
    }
}
