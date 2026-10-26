package kopo.poly2.utill;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileUtill {
    public static String mkdirForDate(String basePath) throws IOException {
        Path path = Path.of(basePath, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        Files.createDirectories(path);
        return path.toString().replace(java.io.File.separatorChar, '/');
    }
}
