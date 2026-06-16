package bryan.app;

import bryan.app.duplicates.DuplicateFileCommand;
import bryan.app.largefolders.LargeFoldersCommand;
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
                DuplicateFileCommand.class,
                LargeFoldersCommand.class,
                RecycleBinCommand.class,
                TempCommand.class
        })
public class DiskCleanerCommand { }
