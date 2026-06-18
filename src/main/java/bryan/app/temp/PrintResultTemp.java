package bryan.app.temp;

import bryan.app.common.FormatSize;
import bryan.app.common.ReportFormatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class PrintResultTemp {

    @Inject
    FormatSize formatSize;

    public void execute(AtomicLong totalSize, AtomicLong totalFiles, AtomicLong skipped, List<Path> tempPaths, boolean isDelete) {

        String title = isDelete ? "TEMP CLEANUP REPORT" : "TEMP FILES REPORT";
        String revisedStr = tempPaths.size() + " revised";
        String filesLabel = isDelete ? totalFiles.get() + " deleted" : totalFiles.get() + " files";
        String sizeLabel = isDelete ? formatSize.execute(totalSize.get()) + " freed" : formatSize.execute(totalSize.get());
        String skippedLabel = skipped.get() + " skipped";

        String linePath = "Path:      " + revisedStr;
        String lineFiles = "Files:     " + filesLabel;
        String lineSize = "Size:      " + sizeLabel;
        String lineSkipped = "Skipped:   " + skippedLabel;

        ReportFormatter formatter = new ReportFormatter(Arrays.asList(
                title, linePath, lineFiles, lineSize, lineSkipped
        ));

        String report = String.format("%n") + formatter.getSeparator() + String.format("%n") +
                formatter.formatLine(title) +
                formatter.getSeparator() + String.format("%n") +
                formatter.formatLine(linePath) +
                formatter.formatLine(lineFiles) +
                formatter.formatLine(lineSize) +
                formatter.formatLine(lineSkipped) +
                formatter.getSeparator();

        System.out.println(report);
    }
}