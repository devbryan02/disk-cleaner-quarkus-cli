package bryan.app.largefolders;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class LargeFoldersService {

    @Inject ScanPaths scanPaths;
    @Inject ResolvePaths resolvePaths;

    public record ScanResult(List<LargeFileResult> files, AtomicLong skipped) {}

    public ScanResult scan() {
        AtomicLong skipped = new AtomicLong();
        long minSize = 100 * 1024 * 1024; // 100 MB

        List<LargeFileResult> allResults = resolvePaths.execute()
                .stream()
                .flatMap(path -> scanPaths.execute(path, minSize, skipped).stream())
                .toList();

        return new ScanResult(allResults, skipped);
    }

}
