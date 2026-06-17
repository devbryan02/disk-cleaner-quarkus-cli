package bryan.app.recyclebin;

import bryan.app.common.DeleteDirectory;
import bryan.app.common.ScanDirectory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@ApplicationScoped
public class RecycleBinService {

    @Inject
    ScanDirectory scanDirectory;
    @Inject
    PrintRecycleBinResult printRecycleBinResult;
    @Inject
    DeleteDirectory deleteDirectory;

    public RecycleBinResult scanRecycleBin() {

        AtomicLong totalSize = new AtomicLong();
        AtomicLong totalFiles = new AtomicLong();
        AtomicLong skipped = new AtomicLong();

        Path pathBin = proccessPath(
                path -> scanDirectory.execute(path, totalSize, totalFiles, skipped)
        );

        return new RecycleBinResult(pathBin, totalSize, totalFiles, skipped, false);

    }

    public RecycleBinResult deleteRecycleBin() {
        AtomicLong totalSizeFreed = new AtomicLong();
        AtomicLong totalFilesDeleted = new AtomicLong();
        AtomicLong skipped = new AtomicLong();

        Path pathBin = proccessPath(
                path -> deleteDirectory.execute(path, totalFilesDeleted, totalSizeFreed, skipped)
        );

        return new RecycleBinResult(pathBin, totalSizeFreed, totalFilesDeleted, skipped, true);
    }

    public void printResult(RecycleBinResult result) {
        printRecycleBinResult.execute(result.path(), result.size(), result.files(), result.skipped(), result.isDelete());
    }

    private Path proccessPath(Consumer<Path> action){
        Path pathBin = Path.of("C:\\$Recycle.Bin");
        if(Files.exists(pathBin)) action.accept(pathBin);
        return pathBin;
    }

}
