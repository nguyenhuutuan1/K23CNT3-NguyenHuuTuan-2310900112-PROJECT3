package K23cnt3.nht._2.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class FileCleanupTask {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.temp-file-lifetime:24}")
    private int tempFileLifetimeHours;

    @Scheduled(cron = "0 0 2 * * ?") // Chạy mỗi ngày lúc 2:00 AM
    public void cleanupTempFiles() {
        Path tempDir = Paths.get(uploadDir).resolve("temp").toAbsolutePath().normalize();

        if (!Files.exists(tempDir)) {
            return;
        }

        try {
            Files.walkFileTree(tempDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {

                    LocalDateTime fileTime = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(attrs.lastModifiedTime().toMillis()),
                            ZoneId.systemDefault()
                    );

                    LocalDateTime cutoffTime = LocalDateTime.now()
                            .minusHours(tempFileLifetimeHours);

                    if (fileTime.isBefore(cutoffTime)) {
                        Files.delete(file);
                        System.out.println("Deleted old temp file: " + file);
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                        throws IOException {
                    // Xóa thư mục rỗng
                    if (Files.list(dir).count() == 0 && !dir.equals(tempDir)) {
                        Files.delete(dir);
                        System.out.println("Deleted empty directory: " + dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException e) {
            System.err.println("Error cleaning up temp files: " + e.getMessage());
        }
    }
}