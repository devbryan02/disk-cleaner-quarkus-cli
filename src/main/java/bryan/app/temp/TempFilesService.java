package bryan.app.temp;

import bryan.app.common.DeleteDirectory;
import bryan.app.common.ScanDirectory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@ApplicationScoped
public class TempFilesService {

    @Inject
    PrintResultTemp printResultTemp;
    @Inject
    ResolveTempFiles resolveTempFiles;
    @Inject
    ScanDirectory scanDirectory;
    @Inject
    DeleteDirectory deleteDirectory;

    public TempFilesResult scanTempFiles() {
        AtomicLong totalSize = new AtomicLong();
        AtomicLong totalFiles = new AtomicLong();
        AtomicLong skippedPaths = new AtomicLong();

        List<Path> tempPaths = processTempPaths(path ->
                scanDirectory.execute(path, totalSize, totalFiles, skippedPaths)
        );

        return new TempFilesResult(totalSize, totalFiles, skippedPaths, tempPaths, false);
    }

    public TempFilesResult deleteTempFiles() {
        AtomicLong totalSizeFreed = new AtomicLong();
        AtomicLong totalFilesDeleted = new AtomicLong();
        AtomicLong skipped = new AtomicLong();

        List<Path> tempPaths = processTempPaths(path ->
                deleteDirectory.execute(path, totalFilesDeleted, totalSizeFreed, skipped)
        );

        return new TempFilesResult(totalSizeFreed, totalFilesDeleted, skipped, tempPaths, true);
    }

    public void printResult(TempFilesResult result) {
        printResultTemp.execute(result.size(), result.files(), result.skipped(), result.paths(), result.isDelete());
    }

    private List<Path> processTempPaths(Consumer<Path> action) {
        List<Path> tempPaths = resolveTempFiles.execute();

        for (Path path : tempPaths) if (Files.exists(path)) action.accept(path);

        return tempPaths;
    }
}
