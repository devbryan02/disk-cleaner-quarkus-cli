package bryan.app.temp;

import bryan.app.common.FormatSize;
import bryan.app.common.ReportFormatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class PrintResultTemp {

    @Inject FormatSize formatSize;

    public void execute(AtomicLong totalSize, AtomicLong totalFiles, AtomicLong skipped, List<Path> tempPaths, boolean isDelete) {
        String title = isDelete ? "TEMP CLEANUP REPORT" : "TEMP FILES REPORT";
        String revisedStr = tempPaths.size() + " revised";
        String filesLabel = isDelete ? totalFiles.get() + " deleted" : totalFiles.get() + " files";
        String sizeLabel = isDelete ? formatSize.execute(totalSize.get()) + " freed" : formatSize.execute(totalSize.get());
        String skippedLabel = skipped.get() + " skipped";

        List<String> lines = List.of(
                "Path:      " + revisedStr,
                "Files:     " + filesLabel,
                "Size:      " + sizeLabel,
                "Skipped:   " + skippedLabel
        );

        System.out.println(ReportFormatter.buildReport(title, lines));
    }
}
