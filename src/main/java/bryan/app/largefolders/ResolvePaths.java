package bryan.app.largefolders;

import jakarta.enterprise.context.ApplicationScoped;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@ApplicationScoped
public class ResolvePaths {

    public List<Path> execute() {

        String home = System.getProperty("user.home");

        if(home == null) return List.of();

        return Stream.of(
                resolve(home, "Documents", "Documentos"),
                resolve(home, "Downloads", "Descargas"),
                resolve(home, "Videos",    "Videos"),
                resolve(home, "Pictures",  "Imágenes", "Imagenes")
        ).filter(Objects::nonNull)
                .toList();
    }

    private Path resolve(String home, String... candidates){
        for(String name: candidates){
            Path path = Path.of(home, name);
            if(Files.exists(path)) return path;
        }
        return null;
    }
}
