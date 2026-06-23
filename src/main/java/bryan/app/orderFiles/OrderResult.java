package bryan.app.orderFiles;

import java.nio.file.Path;
import java.util.Map;

public record OrderResult(
        Path targetPath,
        Map<String, Long> filesByCategory,
        Map<String, Long> sizeByCategory,
        long totalMoved,
        long totalSizeMoved,
        long skipped
) {}
