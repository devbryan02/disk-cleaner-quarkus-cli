package bryan.app.largefolders;

import bryan.app.common.ProcessWithLoading;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;

@Command(
        name = "large-folders",
        description = "Busca carpetas pesadas"
)
public class LargeFoldersCommand implements Runnable{

    @Inject LargeFoldersService largeFoldersService;
    @Inject ProcessWithLoading processWithLoading;
    @Inject PrintLargeFoldersResult print;

    @Override
    public void run() {
        LargeFoldersService.ScanResult[] result = new LargeFoldersService.ScanResult[1];

        processWithLoading.execute(
                "Buscando archivos grandes",
                () -> result[0] = largeFoldersService.scan()
        );

        print.execute(result[0].files(), result[0].skipped());
    }
}
