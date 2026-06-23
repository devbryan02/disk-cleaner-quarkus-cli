package bryan.app.orderFiles;

import bryan.app.common.FormatSize;
import bryan.app.common.ReportFormatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PrintOrderResult {

    @Inject
    FormatSize formatSize;

    public void execute(OrderResult result, boolean dryRun) {
        String title = dryRun ? "PREVIEW - FILES TO ORGANIZE" : "ORDER REPORT";

        List<String> allLines = new ArrayList<>();
        allLines.add(title);
        allLines.add("Target-  " + result.targetPath());

        for (Map.Entry<String, Long> entry : result.filesByCategory().entrySet()) {
            long size = result.sizeByCategory().getOrDefault(entry.getKey(), 0L);
            allLines.add(entry.getKey() + "-   " + entry.getValue() + " files  ("
                    + formatSize.execute(size) + ")");
        }

        allLines.add("");
        allLines.add("Total-   " + result.totalMoved() + " files  ("
                + formatSize.execute(result.totalSizeMoved()) + ")");
        allLines.add("Skipped- " + result.skipped() + " (unknown or errors)");

        var formatter = new ReportFormatter(allLines);

        StringBuilder report = new StringBuilder();
        report.append(String.format("%n"));
        report.append(formatter.getSeparator()).append(String.format("%n"));
        report.append(formatter.formatLine(allLines.get(0)));
        report.append(formatter.getSeparator()).append(String.format("%n"));
        for (int i = 1; i < allLines.size(); i++) {
            report.append(formatter.formatLine(allLines.get(i)));
        }
        report.append(formatter.getSeparator());

        System.out.println(report);
    }
}
