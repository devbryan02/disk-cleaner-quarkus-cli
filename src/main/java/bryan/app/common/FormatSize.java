package bryan.app.common;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FormatSize {

    public String execute(long bytes) {
        if(bytes < 1024 ) return bytes + " B";
        if(bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        if(bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)) + " MB";
        return (bytes / (1024 * 1024 * 1024)) + " GB";
    }

}
