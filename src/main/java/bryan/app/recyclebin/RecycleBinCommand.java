package bryan.app.recyclebin;

import picocli.CommandLine.Command;

@Command(
        name = "recycle-bin",
        description = "Limpia la papelera"
)
public class RecycleBinCommand implements Runnable{

    @Override
    public void run() {
        System.out.println("Limpiando papelera");
    }

}
