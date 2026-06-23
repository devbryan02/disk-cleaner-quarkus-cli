package bryan.app.recyclebin;

import bryan.app.common.FormatSize;
import bryan.app.common.ReportFormatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class PrintRecycleBinResult {

    @Inject FormatSize formatSize;

    public void execute(Path path, AtomicLong size, AtomicLong files, AtomicLong skipped, boolean isDelete) {
        String title = isDelete ? "RECYCLE BIN CLEANUP REPORT" : "RECYCLE BIN FILES REPORT";
        String filesLabel = isDelete ? files.get() + " cleaned" : files.get() + " files";
        String sizeLabel = isDelete ? formatSize.execute(size.get()) + " freed" : formatSize.execute(size.get());
        String skippedLabel = skipped.get() + " --skipped";

        List<String> lines = List.of(
                "Path-      " + path,
                "Files-     " + filesLabel,
                "Size-      " + sizeLabel,
                "Skipped-   " + skippedLabel
        );

        System.out.println(ReportFormatter.buildReport(title, lines));
    }
}
