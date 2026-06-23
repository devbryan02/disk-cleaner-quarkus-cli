package bryan.app.orderfiles;

import bryan.app.common.ProcessWithLoading;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;

@Command(
        name = "order",
        description = "Organiza archivos del directorio Downloads por tipo"
)
public class OrderFilesCommand implements Runnable {

    @Inject
    OrderService orderService;

    @Inject
    ProcessWithLoading processWithLoading;

    @Inject
    PrintOrderResult printOrderResult;

    @Option(
            names = {"-p", "--path"},
            description = "Directorio a organizar (default: Downloads)"
    )
    private Path customPath;

    @Option(
            names = {"--dry-run"},
            description = "Solo mostrar qué se moveria, sin ejecutar"
    )
    private boolean dryRun;

    @Override
    public void run() {
        Path targetPath = (customPath != null)
                ? customPath
                : Path.of(System.getProperty("user.home"), "Downloads");

        OrderResult[] results = new OrderResult[1];

        String msg = dryRun
                ? "Simulando organization de " + targetPath
                : "Organizando " + targetPath;

        processWithLoading.execute(
                msg,
                () -> results[0] = orderService.execute(targetPath, dryRun)
        );

        printOrderResult.execute(results[0], dryRun);
    }
}
