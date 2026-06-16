package bryan.app.temp;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ResolveTempFiles {

    public List<Path> execute() {
        List<Path> paths = new ArrayList<>();

        paths.add(Path.of(System.getProperty("java.io.tmpdir")));

        String temp = System.getenv("TEMP");
        String tmp = System.getenv("TMP");
        if (temp != null) paths.add(Path.of(temp));
        if (tmp != null) paths.add(Path.of(tmp));

        String sysRoot = System.getenv("SystemRoot");
        if (sysRoot == null) {
            sysRoot = System.getenv("windir");
        }

        if (sysRoot != null) {
            paths.add(Path.of(sysRoot, "Temp"));
            paths.add(Path.of(sysRoot, "Prefetch"));
        }

        String localAppData = System.getenv("LOCALAPPDATA");
        if (localAppData != null) {
            paths.add(Path.of(localAppData, "Microsoft", "Windows", "INetCache"));
        }


        return paths.stream()
                .map(p -> {try { return p.toRealPath(); } catch (IOException e) { return p.toAbsolutePath();}})
                .distinct()
                .toList();
    }
}
