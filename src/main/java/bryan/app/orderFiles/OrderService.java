package bryan.app.orderFiles;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class OrderService {

    private static final Map<String, String> EXTENSION_MAP = new LinkedHashMap<>();

    static {
        EXTENSION_MAP.put("pdf", "PDF");
        EXTENSION_MAP.put("doc", "Word");
        EXTENSION_MAP.put("docx", "Word");
        EXTENSION_MAP.put("xls", "Excel");
        EXTENSION_MAP.put("xlsx", "Excel");
        EXTENSION_MAP.put("ppt", "PPT");
        EXTENSION_MAP.put("pptx", "PPT");
        EXTENSION_MAP.put("txt", "Textos");
        EXTENSION_MAP.put("csv", "CSV");

        EXTENSION_MAP.put("jpg", "Imagenes");
        EXTENSION_MAP.put("jpeg", "Imagenes");
        EXTENSION_MAP.put("png", "Imagenes");
        EXTENSION_MAP.put("gif", "Imagenes");
        EXTENSION_MAP.put("bmp", "Imagenes");
        EXTENSION_MAP.put("svg", "Imagenes");
        EXTENSION_MAP.put("webp", "Imagenes");

        EXTENSION_MAP.put("mp4", "Videos");
        EXTENSION_MAP.put("avi", "Videos");
        EXTENSION_MAP.put("mkv", "Videos");
        EXTENSION_MAP.put("mov", "Videos");

        EXTENSION_MAP.put("mp3", "Musica");
        EXTENSION_MAP.put("wav", "Musica");
        EXTENSION_MAP.put("flac", "Musica");

        EXTENSION_MAP.put("zip", "Archivos");
        EXTENSION_MAP.put("rar", "Archivos");
        EXTENSION_MAP.put("7z", "Archivos");
        EXTENSION_MAP.put("tar", "Archivos");

        EXTENSION_MAP.put("exe", "Instaladores");
        EXTENSION_MAP.put("msi", "Instaladores");

        EXTENSION_MAP.put("java", "Codigo");
        EXTENSION_MAP.put("py", "Codigo");
        EXTENSION_MAP.put("js", "Codigo");
        EXTENSION_MAP.put("html", "Codigo");
        EXTENSION_MAP.put("json", "Codigo");
    }

    public OrderResult execute(Path targetDir, boolean dryRun) {

        Map<String, AtomicLong> filesByCategory = new LinkedHashMap<>();
        Map<String, AtomicLong> sizeByCategory = new LinkedHashMap<>();
        AtomicLong totalMoved = new AtomicLong();
        AtomicLong totalSizeMoved = new AtomicLong();
        AtomicLong skipped = new AtomicLong();

        try {
            Files.walkFileTree(targetDir, new SimpleFileVisitor<>() {

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (dir.equals(targetDir)) return FileVisitResult.CONTINUE;
                    return FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String ext = getExtension(file.getFileName().toString());
                    String category = EXTENSION_MAP.get(ext);

                    if (category == null) {
                        skipped.incrementAndGet();
                        return FileVisitResult.CONTINUE;
                    }

                    Path categoryDir = targetDir.resolve(category);
                    Path target = categoryDir.resolve(file.getFileName());

                    try {
                        if (Files.notExists(categoryDir)) {
                            Files.createDirectory(categoryDir);
                        }

                        if (Files.exists(target)) {
                            skipped.incrementAndGet();
                            return FileVisitResult.CONTINUE;
                        }

                        if (!dryRun) {
                            Files.move(file, target, StandardCopyOption.ATOMIC_MOVE);
                        }

                        long size = attrs.size();
                        filesByCategory.computeIfAbsent(category, k -> new AtomicLong()).incrementAndGet();
                        sizeByCategory.computeIfAbsent(category, k -> new AtomicLong()).addAndGet(size);
                        totalMoved.incrementAndGet();
                        totalSizeMoved.addAndGet(size);

                    } catch (IOException e) {
                        skipped.incrementAndGet();
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    skipped.incrementAndGet();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error al leer directorio: " + targetDir);
        }

        Map<String, Long> filesFinal = new LinkedHashMap<>();
        Map<String, Long> sizeFinal = new LinkedHashMap<>();

        filesByCategory.forEach((k, v) -> filesFinal.put(k, v.get()));
        sizeByCategory.forEach((k, v) -> sizeFinal.put(k, v.get()));

        return new OrderResult(targetDir, filesFinal, sizeFinal,
                totalMoved.get(), totalSizeMoved.get(), skipped.get());
    }

    private String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i == -1) ? "" : fileName.substring(i + 1).toLowerCase();
    }
}
