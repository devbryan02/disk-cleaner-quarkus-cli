package bryan.app.largefolders;

import bryan.app.common.FormatSize;
import bryan.app.common.ReportFormatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class PrintLargeFoldersResult {

    @Inject FormatSize formatSize;

    private static final int MAX_PATH_LENGTH = 50;
    private static final int SIZE_COLUMN_WIDTH = 12;

    public void execute(List<LargeFileResult> results, AtomicLong skipped) {
        String title = "LARGE FILES REPORT";
        List<String> lines = new ArrayList<>();
        lines.add("Files found: " + results.size());
        lines.add("Skipped:     " + skipped.get());

        if (results.isEmpty()) {
            lines.add("");
            lines.add("No large files found.");
            System.out.println(ReportFormatter.buildReport(title, lines));
            return;
        }

        int maxFileNameLength = "FILE NAME".length();
        for (LargeFileResult r : results) {
            int nameLength = r.path().getFileName().toString().length();
            if (nameLength > maxFileNameLength) {
                maxFileNameLength = nameLength;
            }
        }

        String fmt = "%-" + maxFileNameLength + "s | %" + SIZE_COLUMN_WIDTH + "s | %s";
        String tableHeader = String.format(fmt, "FILE NAME", "SIZE", "FULL PATH");
        String headerUnderline = "-".repeat(maxFileNameLength) + "-+-" + "-".repeat(SIZE_COLUMN_WIDTH) + "-+-" + "-".repeat(MAX_PATH_LENGTH);

        lines.add(tableHeader);
        lines.add(headerUnderline);

        for (LargeFileResult r : results) {
            String fileName = r.path().getFileName().toString();
            String sizeStr = formatSize.execute(r.size());
            String shrunkPath = shrinkPath(r.path().toString());
            lines.add(String.format(fmt, fileName, sizeStr, shrunkPath));
        }

        System.out.println(ReportFormatter.buildReport(title, lines));
    }

    private String shrinkPath(String path) {
        if (path == null || path.length() <= MAX_PATH_LENGTH) {
            return path;
        }
        return "..." + path.substring(path.length() - (MAX_PATH_LENGTH - 3));
    }
}
