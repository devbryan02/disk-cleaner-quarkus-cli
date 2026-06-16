package bryan.app.duplicates;

import picocli.CommandLine;

@CommandLine.Command(
        name = "duplicates",
        description = "Busca archivos duplicados"
)
public class DuplicateFileCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Buscando archivos duplicados");
    }
}
