# disk-cleaner-cli

CLI tool for cleaning and organizing disk space on Windows, built with Quarkus and Picocli.

## Commands

| Command | Description |
|---|---|
| `cleanb large-folders` | Finds files >= 100MB in user directories |
| `cleanb temp` | Scans or deletes temp files |
| `cleanb recycle-bin` | Scans or cleans the recycle bin |
| `cleanb order` | Organizes Downloads folder by file type |
| `cleanb --help` | Shows help with all commands |

### `order` — Organize files by type

Scans a directory (default: `~/Downloads`), classifies files by extension, creates category folders, and moves them.

```
cleanb order                        # Organize ~/Downloads
cleanb order --dry-run              # Preview only, no moves
cleanb order -p "D:\Descargas"      # Custom directory
```

**Category folders created:** `PDF`, `Word`, `Excel`, `PPT`, `Textos`, `CSV`, `Imagenes`, `Videos`, `Musica`, `Archivos`, `Instaladores`, `Codigo`.

### `temp` — Temp files

```
cleanb temp              # Scan temp directories
cleanb temp -d           # Delete temp files
```

Scans: Java temp dir, `%TEMP%`, `%TMP%`, `C:\Windows\Temp`, `C:\Windows\Prefetch`, `INetCache`.

### `recycle-bin` — Recycle bin

```
cleanb recycle-bin       # Scan recycle bin
cleanb recycle-bin -d    # Empty recycle bin
```

### `large-folders` — Large files

```
cleanb large-folders     # Find files >= 100MB
```

Scans: Documents, Downloads, Videos, Pictures.

## Run

```shell
./gradlew quarkusDev --quarkus-args='order --dry-run'
```

Build and run:

```shell
./gradlew build
java -jar build/quarkus-app/quarkus-run.jar order
```

## Project structure

```
src/main/java/bryan/app/
├── common/               # Shared utilities
│   ├── FormatSize.java         # Bytes to human-readable
│   ├── ReportFormatter.java    # Boxed console output + buildReport()
│   ├── ProcessWithLoading.java # Spinner animation for long tasks
│   ├── ScanDirectory.java      # Walk tree, count files/sizes
│   └── DeleteDirectory.java    # Walk tree, delete files/dirs
├── largefolders/         # cleanb large-folders
├── orderfiles/           # cleanb order
├── recyclebin/           # cleanb recycle-bin
└── temp/                 # cleanb temp
```

Built with **Java 25**, **Quarkus 3.36**, **Picocli**.
