package bryan.app.orderfiles;

import bryan.app.common.FormatSize;
import bryan.app.common.ReportFormatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PrintOrderResult {

    @Inject FormatSize formatSize;

    public void execute(OrderResult result, boolean dryRun) {
        String title = dryRun ? "PREVIEW - FILES TO ORGANIZE" : "ORDER REPORT";
        List<String> lines = new ArrayList<>();
        lines.add("Target-  " + result.targetPath());

        for (Map.Entry<String, Long> entry : result.filesByCategory().entrySet()) {
            long size = result.sizeByCategory().getOrDefault(entry.getKey(), 0L);
            lines.add(entry.getKey() + "-   " + entry.getValue() + " files  ("
                    + formatSize.execute(size) + ")");
        }

        lines.add("");
        lines.add("Total-   " + result.totalMoved() + " files  ("
                + formatSize.execute(result.totalSizeMoved()) + ")");
        lines.add("Skipped- " + result.skipped() + " (unknown or errors)");

        System.out.println(ReportFormatter.buildReport(title, lines));
    }
}
