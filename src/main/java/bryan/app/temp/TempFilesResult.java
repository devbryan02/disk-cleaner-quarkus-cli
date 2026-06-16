package bryan.app.temp;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public record TempFilesResult(
        AtomicLong size,
        AtomicLong files,
        AtomicLong skipped,
        List<Path> paths,
        boolean isDelete
) {}
