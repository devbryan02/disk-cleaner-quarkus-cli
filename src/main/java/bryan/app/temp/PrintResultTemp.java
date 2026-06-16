package bryan.app.temp;

import bryan.app.common.FormatSize;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class PrintResultTemp {

    @Inject
    FormatSize formatSize;

    public void execute(AtomicLong totalSize, AtomicLong totalFiles, AtomicLong skipped, List<Path> tempPaths, boolean isDelete) {

        String title = isDelete ? "TEMP CLEANUP REPORT" : "TEMP FILES REPORT";
        String filesLabel = isDelete ? totalFiles.get() + " deleted" : totalFiles.get() + " files";
        String sizeLabel = isDelete ? formatSize.execute(totalSize.get()) + " freed" : formatSize.execute(totalSize.get());
        String skippedLabel = skipped.get() + " skipped";

        String separator = "+------------------------------------------+";
        String report = String.format(
                "%n%s%n" +
                        "| %-40s |%n" +
                        "%s%n" +
                        "|  Path:      %-28s |%n" +
                        "|  Files:     %-28s |%n" +
                        "|  Size:      %-28s |%n" +
                        "|  Skipped:   %-28s |%n" +
                        "%s%n",
                separator,
                title,
                separator,
                tempPaths.size() + " revised",
                filesLabel,
                sizeLabel,
                skippedLabel,
                separator
        );

        System.out.println(report);
    }
}
