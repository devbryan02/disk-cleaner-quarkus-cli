package bryan.app.recyclebin;

import bryan.app.common.FormatSize;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

import static bryan.app.common.ReportConstants.*;

@ApplicationScoped
public class PrintRecycleBinResult {


    @Inject
    FormatSize formatSize;

    public void execute(Path path, AtomicLong size, AtomicLong files, AtomicLong skipped, boolean isDelete) {

        String title = isDelete ? "RECYCLE BIN CLEANUP REPORT" : "RECYCLE BIN FILES REPORT";
        String filesLabel = isDelete ? files.get() + " cleaned" : files.get() + " files";
        String sizeLabel = isDelete ? formatSize.execute(size.get()) + " freed" : formatSize.execute(size.get());
        String skippedLabel = skipped.get() + " skipped";

        String report = String.format(
                "%n%s%n" +
                        "| %-" + TITLE_WIDTH + "s |%n" +
                        "%s%n" +
                        "|  Path:      %-" + CONTENT_WIDTH + "s |%n" +
                        "|  Files:     %-" + CONTENT_WIDTH + "s |%n" +
                        "|  Size:      %-" + CONTENT_WIDTH + "s |%n" +
                        "|  Skipped:   %-" + CONTENT_WIDTH + "s |%n" +
                        "%s%n",
                SEPARATOR,
                title,
                SEPARATOR,
                path,
                filesLabel,
                sizeLabel,
                skippedLabel,
                SEPARATOR
        );

        System.out.println(report);

    }
}
