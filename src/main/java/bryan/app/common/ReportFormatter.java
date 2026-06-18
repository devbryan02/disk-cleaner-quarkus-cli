package bryan.app.common;

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

    // Formatea cualquier línea asegurando que se rellene exactamente hasta el contentWidth
    public String formatLine(String text) {
        return String.format("|  %-" + contentWidth + "s  |%n", text);
    }

    // Simplemente concatena la etiqueta y el valor antes de aplicar el formato estructurado
    public String formatLabeledLine(String label, String value) {
        return formatLine(label + value);
    }
}