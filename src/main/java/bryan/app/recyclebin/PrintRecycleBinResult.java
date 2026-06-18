package bryan.app.recyclebin;

import bryan.app.common.FormatSize;
import bryan.app.common.ReportFormatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class PrintRecycleBinResult {

    @Inject
    FormatSize formatSize;

    public void execute(Path path, AtomicLong size, AtomicLong files, AtomicLong skipped, boolean isDelete) {

        String title = isDelete ? "RECYCLE BIN CLEANUP REPORT" : "RECYCLE BIN FILES REPORT";
        String pathStr = path.toString();
        String filesLabel = isDelete ? files.get() + " cleaned" : files.get() + " files";
        String sizeLabel = isDelete ? formatSize.execute(size.get()) + " freed" : formatSize.execute(size.get());
        String skippedLabel = skipped.get() + " --skipped";

        // Las cadenas exactas que se van a mostrar en pantalla
        String linePath = "Path-      " + pathStr;
        String lineFiles = "Files-     " + filesLabel;
        String lineSize = "Size-      " + sizeLabel;
        String lineSkipped = "Skipped-   " + skippedLabel;

        var formatter = new ReportFormatter(Arrays.asList(
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