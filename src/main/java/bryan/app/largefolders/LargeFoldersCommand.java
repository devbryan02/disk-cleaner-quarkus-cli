package bryan.app.largefolders;

import picocli.CommandLine.Command;

@Command(
        name = "large-folders",
        description = "Busca carpetas pesadas"
)
public class LargeFoldersCommand implements Runnable{

    @Override
    public void run() {
        System.out.println("Buscando carpetas pesadas");
    }
}
