package bryan.app;

import bryan.app.largefolders.LargeFoldersCommand;
import bryan.app.orderFiles.OrderFilesCommand;
import bryan.app.recyclebin.RecycleBinCommand;
import bryan.app.temp.TempCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine.Command;

@TopCommand
@Command(
        name = "cleanb",
        description = "Comando base",
        mixinStandardHelpOptions = true,
        subcommands = {
                LargeFoldersCommand.class,
                OrderFilesCommand.class,
                RecycleBinCommand.class,
                TempCommand.class
        })
public class DiskCleanerCommand { }
