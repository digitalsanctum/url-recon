package com.digitalsanctum.recon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class Utils {

    public static String slugify(String url) {
        if (url == null) {
            return "";
        }
        return url.replace("https://", "").replaceAll("[^a-zA-Z0-9]", "_");
    }

    private static void checkDirExists(Path dirPath) throws IOException {
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }

    public static void streamToFile(String content, Path filePath) throws IOException {
        // verify directory exists
        Path parentDir = filePath.getParent();
        checkDirExists(parentDir);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toAbsolutePath().toFile(), true))) {
            writer.write(content);
            writer.newLine();
        }
    }

    public static void saveToFile(String content, Path filePath) throws IOException {

        // verify directory exists
        Path parentDir = filePath.getParent();
        checkDirExists(parentDir);

        // write content to filePath
        Files.writeString(filePath, content);
    }

    public static void logToFile(List<String> lines, Path filePath) throws IOException {

        // verify directory exists
        Path parentDir = filePath.getParent();
        checkDirExists(parentDir);

        // write lines to filePath
        Files.write(filePath, lines);
    }

    public static Optional<String> extractFileNameFromURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
            String path = url.getPath();
            return Optional.of(path.substring(path.lastIndexOf('/') + 1));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
