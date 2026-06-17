package bryan.app.recyclebin;

import bryan.app.common.ProcessWithLoading;
import jakarta.inject.Inject;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

@Command(
        name = "recycle-bin",
        description = "Limpia la papelera"
)
public class RecycleBinCommand implements Runnable{

    @Inject
    RecycleBinService recycleBinService;

    @Inject
    ProcessWithLoading processWithLoading;

    @Option(
            names = {"-d", "--delete"},
            description = "Limpia los archivos de la papelera"
    )
    private boolean delete;

    @Override
    public void run() {
        RecycleBinResult[] results = new RecycleBinResult[1];

        if(delete) {
            processWithLoading.execute(
                    "Limpiando la papelera",
                    () -> results[0] = recycleBinService.deleteRecycleBin()
            );
        } else {
            processWithLoading.execute(
                    "Escaneando la papelera",
                    () -> results[0] = recycleBinService.scanRecycleBin()
            );
        }

        recycleBinService.printResult(results[0]);

    }
}
