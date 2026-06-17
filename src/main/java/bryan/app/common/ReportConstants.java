package bryan.app.common;

public final class ReportConstants {

    private ReportConstants() {}

    public static final int TOTAL_WIDTH = 35;
    public static final int TITLE_WIDTH = TOTAL_WIDTH - 4;
    public static final int CONTENT_WIDTH = TOTAL_WIDTH - 14 - 2;
    public static final String SEPARATOR = "+" + "-".repeat(TOTAL_WIDTH - 2) + "+";

}