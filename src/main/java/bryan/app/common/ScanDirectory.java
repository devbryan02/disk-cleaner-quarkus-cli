package bryan.app.common;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ScanDirectory {

    public void execute(Path path, AtomicLong totalSize, AtomicLong totalFiles, AtomicLong skipped) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    totalFiles.incrementAndGet();
                    totalSize.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    if (exc instanceof AccessDeniedException) skipped.incrementAndGet();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (!Files.isReadable(dir)) {
                        skipped.incrementAndGet();
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error leyendo: " + path);
        }
    }
}
