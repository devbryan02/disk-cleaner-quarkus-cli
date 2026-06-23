package bryan.app.common;

import java.util.ArrayList;
import java.util.List;

public final class ReportFormatter {

    private final int contentWidth;
    private final String separator;
    private static final int PADDING_BORDER = 6;

    public ReportFormatter(List<String> allLines) {
        int maxLen = 0;
        for (String line : allLines) {
            if (line != null && line.length() > maxLen) {
                maxLen = line.length();
            }
        }
        this.contentWidth = maxLen;
        this.separator = "+" + "-".repeat(contentWidth + PADDING_BORDER - 2) + "+";
    }

    public String getSeparator() {
        return separator;
    }

    public String formatLine(String text) {
        return String.format("|  %-" + contentWidth + "s  |%n", text);
    }

    public String formatLabeledLine(String label, String value) {
        return formatLine(label + value);
    }

    public static String buildReport(String title, List<String> bodyLines) {
        List<String> allLines = new ArrayList<>();
        allLines.add(title);
        allLines.addAll(bodyLines);

        var formatter = new ReportFormatter(allLines);
        var sb = new StringBuilder();
        sb.append(String.format("%n"));
        sb.append(formatter.getSeparator()).append(String.format("%n"));
        sb.append(formatter.formatLine(title));
        sb.append(formatter.getSeparator()).append(String.format("%n"));
        for (int i = 1; i < allLines.size(); i++) {
            sb.append(formatter.formatLine(allLines.get(i)));
        }
        sb.append(formatter.getSeparator());
        return sb.toString();
    }
}
