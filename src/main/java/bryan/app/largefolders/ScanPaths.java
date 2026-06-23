package bryan.app.largefolders;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ScanPaths {

    public List<LargeFileResult> execute(Path rootPath, long minSize, AtomicLong skipped) {

        List<LargeFileResult> results = new ArrayList<>();

        if(!Files.exists(rootPath)) return results;

        try{
            Files.walkFileTree(rootPath, new SimpleFileVisitor<>() {

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                    if(attrs.size() >= minSize)
                        results.add(new LargeFileResult(file, attrs.size()));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult visitFileFailed(Path file, IOException ex){
                    if( ex instanceof AccessDeniedException) skipped.incrementAndGet();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                @SuppressWarnings("NullableProblems")
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){
                    if(!Files.isReadable(dir)){
                        skipped.incrementAndGet();
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

            });
        }catch (IOException e){
            System.err.println("Error al leer el directorio: " + rootPath + " - " + e.getMessage());
        }
        return results;
    }

}
