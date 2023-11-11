package org.lisasp.competition.results.imports.em2023;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern ResultPattern = Pattern.compile(".*<a href=\"(.*pdf)\" target=\"_blank\">Results</a>.*");

    public static void main(String[] args) throws IOException {
        importNationals();
        // importInterclubs();
        // importMasters();
    }

    public static void importNationals() throws IOException {
        String[] names = {"day2nmresults.pdf", "day2vmresults.pdf", "day3nmresults.pdf", "day3vmresults.pdf"};
        for (String name : names) {
            Path filename = Path.of("data", "downloads", "Nationals", name);
            Path textFile = changeExtension(filename, "txt");
            if (!Files.exists(textFile)) {
                Files.write(textFile, extractTextFromPdf(filename), StandardOpenOption.CREATE);
            }

            System.out.println(textFile);

            ResultParser parser = new ResultParser();
            parser.push(Files.readAllLines(textFile));
            Arrays.stream(parser.read()).forEach(System.out::println);
        }
    }

    public static void importMasters() throws IOException {
        String[] names = {"day1nmresults.pdf", "day1vmresults.pdf", "day2nmresults.pdf", "day2vmresults.pdf"};
        for (String name : names) {
            Path filename = Path.of("data", "downloads", "Masters", name);
            Path textFile = changeExtension(filename, "txt");
            if (!Files.exists(textFile)) {
                Files.write(textFile, extractTextFromPdf(filename), StandardOpenOption.CREATE);
            }

            System.out.println(textFile);

            ResultParser parser = new ResultParser();
            parser.push(Files.readAllLines(textFile));
            Arrays.stream(parser.read()).forEach(System.out::println);
        }
    }

    public static void importInterclubs() throws IOException {
        Downloader downloader = new HttpDownloader("https://www.toptime.be/oresults/");

        Path indexPath = Path.of("data", "downloads", "Interclubs", "index.html");
        if (!indexPath.toFile().exists()) {
            Files.write(indexPath, downloader.download("index_us.html"), StandardOpenOption.CREATE);
        }

        String index = Files.readString(indexPath).replace("</th>", "</th>\r\n").replace("</td>", "</td>\r\n").replace("</tr>", "</tr>\r\n");


        String[] names = Arrays.stream(index.split("\r\n")).map(r -> r.replace("\t", " ").trim()).filter(r -> !r.isBlank())
                .map(row2 -> {
                    Matcher resultMatcher = ResultPattern.matcher(row2);
                    return resultMatcher.matches() ? resultMatcher.group(1) : "";
                }).filter(r -> !r.isBlank()).sorted().toArray(String[]::new);
        for (String name : names) {
            Path filename = Path.of("data", "downloads", "Interclubs", name);
            if (!Files.exists(filename)) {
                System.out.println("  " + name);
                Files.write(filename, downloader.download(name), StandardOpenOption.CREATE);
            }
            Path textFile = changeExtension(filename, "txt");
            if (!Files.exists(textFile)) {
                Files.write(textFile, extractTextFromPdf(filename), StandardOpenOption.CREATE);
            }

            System.out.println(textFile);

            ResultParser parser = new ResultParser();
            parser.push(Files.readAllLines(textFile));
            Arrays.stream(parser.read()).forEach(System.out::println);
        }
    }

    public static Path changeExtension(Path source, String extension) {
        String newFileName = changeExtension(extension, source.getFileName().toString());
        return source.getParent().resolve(newFileName);
    }

    public static String changeExtension(String extension, String fileName) {
        return String.format("%s.%s", fileName.substring(0, fileName.lastIndexOf('.')), extension);
    }

    private static List<String> extractTextFromPdf(Path filename) throws IOException {
        try (PDDocument pdf = Loader.loadPDF(filename.toFile())) {

            //Instantiate PDFTextStripper class
            PDFTextStripper pdfStripper = new PDFTextStripper();
            // pdfStripper.setAddMoreFormatting(true);
            // pdfStripper.setArticleEnd(";");
            // pdfStripper.setArticleStart(";");
            // pdfStripper.setParagraphEnd(";");
            // pdfStripper.setParagraphStart(";");
            // pdfStripper.setPageEnd(";");
            pdfStripper.setWordSeparator(";");

            //Retrieving text from PDF document
            return Arrays.stream(pdfStripper.getText(pdf)
                            .replace("\r", "")
                            .split("\n"))
                    .filter(line -> !isInfoLine(line))
                    .toList();
        }
    }

    private static final Set<String> InfoLineStarts = Set.of("European Record", "World Record", "Splash Meet Manager", "European Championships Lifesaving Interclub", "Brugge/Blankenberge", "Points: ILS 2016");

    private static boolean isInfoLine(String line) {
        for (String start : InfoLineStarts) {
            if (line.startsWith(start)) {
                return true;
            }
        }
        return false;
    }
}