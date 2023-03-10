package org.lisasp.results.imports.rescue2022;

import lombok.extern.slf4j.Slf4j;
import org.lisasp.results.imports.rescue2022.overview.OverviewDownloader;

import java.io.IOException;

@Slf4j
public class Rescue2022Downloader {

    private static void download(String competition, String type) throws IOException {
        HttpDownloader downloader = new HttpDownloader("https://lifesavingwc2022.microplustimingservices.com/export/LWC2022_%s/NU/%s");
        new OverviewDownloader(downloader).download(competition, type);
    }

    public static void main(String[] args) throws IOException {
        download("Rescue 2022", "Open");
        download("Rescue 2022","Youth");
        download("Rescue 2022","Master");
        download("RescueCup 2022", "RescueCup");
    }
}
