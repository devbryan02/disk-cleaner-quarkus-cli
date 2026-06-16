package bryan.app.common;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class DeleteDirectory {

    public void execute(Path rootPath, AtomicLong totalFilesDeleted, AtomicLong totalSizeFreed, AtomicLong skipped) {
        if (!Files.exists(rootPath)) return;

        try {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(rootPath)) {

                for (Path child : stream) {
                    Files.walkFileTree(child, new SimpleFileVisitor<>() {

                        @Override
                        @SuppressWarnings("NullableProblems")
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            try {
                                long fileSize = attrs.size();
                                Files.delete(file);
                                totalFilesDeleted.incrementAndGet();
                                totalSizeFreed.addAndGet(fileSize);
                            } catch (IOException e) {
                                if (e instanceof AccessDeniedException) {
                                    skipped.incrementAndGet();
                                }
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        @SuppressWarnings("NullableProblems")
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                            if (exc == null) {
                                try {
                                    Files.delete(dir);
                                    totalFilesDeleted.incrementAndGet();
                                } catch (IOException e) {
                                    if (e instanceof AccessDeniedException) {
                                        skipped.incrementAndGet();
                                    }
                                }
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        @SuppressWarnings("NullableProblems")
                        public FileVisitResult visitFileFailed(Path file, IOException exc) {
                            if (exc instanceof AccessDeniedException) {
                                skipped.incrementAndGet();
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        @SuppressWarnings("NullableProblems")
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                            if (!Files.isReadable(dir) || !Files.isWritable(dir)) {
                                skipped.incrementAndGet();
                                return FileVisitResult.SKIP_SUBTREE;
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
            }
        } catch (IOException e) {
            System.err.println("Error al acceder al directorio raíz: " + rootPath);
        }
    }
}
