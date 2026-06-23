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

    // Ajustamos el límite del path para que quepa cómodamente en una sola fila tabular
    private static final int MAX_PATH_LENGTH = 50;
    private static final int SIZE_COLUMN_WIDTH = 12;

    public void execute(List<LargeFileResult> results, AtomicLong skipped) {

        String title = "LARGE FILES REPORT";
        String filesFoundStr = results.size() + " files";
        String skippedStr = skipped.get() + " skipped";

        String lineFiles = "Files found: " + filesFoundStr;
        String lineSkipped = "Skipped:     " + skippedStr;

        // 1. Determinar el nombre de archivo más largo para la primera columna
        int maxFileNameLength = "FILE NAME".length();
        for (LargeFileResult r : results) {
            int nameLength = r.path().getFileName().toString().length();
            if (nameLength > maxFileNameLength) {
                maxFileNameLength = nameLength;
            }
        }

        // 2. Construir la fila de cabecera de la tabla de forma segura
        String tableHeader = String.format("%-" + maxFileNameLength + "s | %" + SIZE_COLUMN_WIDTH + "s | %s",
                "FILE NAME", "SIZE", "FULL PATH");

        // Crear una línea divisoria interna para separar la cabecera de los datos
        String headerUnderline = "-".repeat(maxFileNameLength) + "-+-" + "-".repeat(SIZE_COLUMN_WIDTH) + "-+-" + "-".repeat(MAX_PATH_LENGTH);

        // 3. Preparar todas las líneas para que el ReportFormatter calcule el ancho de la caja contenedora
        List<String> allLines = new ArrayList<>();
        allLines.add(title);
        allLines.add(lineFiles);
        allLines.add(lineSkipped);
        allLines.add(tableHeader);

        // Formatear cada fila de datos de antemano
        List<String> dataRows = new ArrayList<>();
        for (LargeFileResult r : results) {
            String fileName = r.path().getFileName().toString();
            String sizeStr = formatSize.execute(r.size());
            String shrunkPath = shrinkPath(r.path().toString());

            String row = String.format("%-" + maxFileNameLength + "s | %" + SIZE_COLUMN_WIDTH + "s | %-" + MAX_PATH_LENGTH + "s",
                    fileName, sizeStr, shrunkPath);

            dataRows.add(row);
            allLines.add(row);
        }

        ReportFormatter formatter = new ReportFormatter(allLines);

        // 4. Construcción y renderizado del reporte estructurado
        StringBuilder report = new StringBuilder();
        report.append(String.format("%n"))
                .append(formatter.getSeparator()).append(String.format("%n"))
                .append(formatter.formatLine(title))
                .append(formatter.getSeparator()).append(String.format("%n"))
                .append(formatter.formatLine(lineFiles))
                .append(formatter.formatLine(lineSkipped))
                .append(formatter.getSeparator()).append(String.format("%n"));

        // Si no hay archivos, cerramos la caja temprano de forma limpia
        if (results.isEmpty()) {
            report.append(formatter.formatLine("No large files found."))
                    .append(formatter.getSeparator());
            System.out.println(report);
            return;
        }

        // Inyección de la estructura tabular dentro de la caja externa
        report.append(formatter.formatLine(tableHeader))
                .append(formatter.formatLine(headerUnderline));

        for (String row : dataRows) {
            report.append(formatter.formatLine(row));
        }

        report.append(formatter.getSeparator());
        System.out.println(report);
    }

    /**
     * Recorta un path si supera la longitud máxima, manteniendo la parte final.
     */
    private String shrinkPath(String path) {
        if (path == null || path.length() <= MAX_PATH_LENGTH) {
            return path;
        }
        int keepLength = MAX_PATH_LENGTH - 3;
        return "..." + path.substring(path.length() - keepLength);
    }
}