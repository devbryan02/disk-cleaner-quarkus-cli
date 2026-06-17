package bryan.app.recyclebin;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

public record RecycleBinResult(
        Path path,
        AtomicLong size,
        AtomicLong files,
        AtomicLong skipped,
        boolean isDelete
) { }
