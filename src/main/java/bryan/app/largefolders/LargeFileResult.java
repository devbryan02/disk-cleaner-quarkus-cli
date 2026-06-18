package bryan.app.largefolders;

import java.nio.file.Path;

public record LargeFileResult(Path path, long size) { }
