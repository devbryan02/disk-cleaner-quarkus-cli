package bryan.app.temp;

import bryan.app.common.ProcessWithLoading;
import jakarta.inject.Inject;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

@Command(
        name = "temp",
        description = "Limpia o muestra los archivos temporales"
)
public class TempCommand implements Runnable {

    @Inject
    TempFilesService tempFilesService;

    @Inject
    ProcessWithLoading processWithLoading;

    @Option(
            names = {"-d", "--delete"},
            description = "Elimina los archivos temporales"
    )
    private boolean delete;

    @Override
    public void run() {
        TempFilesResult[] result = new TempFilesResult[1];

        if (delete) {
            processWithLoading.execute(
                    "Eliminando archivos temporales",
                    () -> result[0] = tempFilesService.deleteTempFiles()
            );
        } else {
            processWithLoading.execute(
                    "Escaneando archivos temporales",
                    () -> result[0] = tempFilesService.scanTempFiles()
            );
        }

        tempFilesService.printResult(result[0]);
    }
}
