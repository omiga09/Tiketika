package com.tiketika.engine.common.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.List;

@Service
public class FileStorageService {

    private final Path root;
    private static final SecureRandom random = new SecureRandom();
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyz0123456789";

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.root.resolve("public/images"));
            Files.createDirectories(this.root.resolve("private/contracts"));
            Files.createDirectories(this.root.resolve("private/keys"));
        } catch (IOException e) {
            throw new RuntimeException("Upload folder setup failed", e);
        }
    }

    public List<String> saveFiles(List<MultipartFile> files, String category) {
        if (files == null || files.isEmpty()) return List.of();
        return files.stream().map(f -> saveFile(f, category)).toList();
    }

    public String saveFile(MultipartFile file, String category) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File must not exceed 5MB");
        }

        String safeCategory = switch (category) {
            case "images" -> "public/images";
            case "contracts" -> "private/contracts";
            case "keys" -> "private/keys";
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };

        String ext = getExtension(file.getOriginalFilename());
        String baseName = getBaseName(file.getOriginalFilename());
        baseName = baseName.toLowerCase().replaceAll("\\s+", "_");

        String randomPrefix = generateRandomId(12);
        String filename = randomPrefix + "_" + baseName + ext;

        Path target = root.resolve(safeCategory).resolve(filename);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + safeCategory + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public void deleteByDbPath(String dbPath) {
        if (dbPath == null || dbPath.isBlank()) return;
        Path p = resolvePath(dbPath);
        if (!isUnderRoot(p)) return; // safety
        try {
            Files.deleteIfExists(p);
        } catch (IOException e) {
            // log error if needed
        }
    }

    public Path resolvePath(String dbPath) {
        // we only store under /uploads/
        String rel = dbPath.replaceFirst("^/+", "")
                .replaceFirst("^uploads/", "");
        return this.root.resolve(rel).normalize();
    }

    private boolean isUnderRoot(Path candidate) {
        Path n = candidate.normalize();
        return n.startsWith(this.root);
    }

    private String getExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".")).toLowerCase();
        }
        return "";
    }

    private String getBaseName(String filename) {
        if (filename == null) return "file";
        int index = filename.lastIndexOf(".");
        return index != -1 ? filename.substring(0, index) : filename;
    }

    private String generateRandomId(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }
}
