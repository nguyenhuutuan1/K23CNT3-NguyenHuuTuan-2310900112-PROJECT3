package K23cnt3.nht._2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${app.file.upload-dir:src/main/resources/static/images}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) throws IOException {
        // Kiểm tra file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Lấy tên file gốc và extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Tạo tên file duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Lưu file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);

        return uniqueFilename;
    }

    public String getFileUrl(String filename) {
        return "/images/" + filename;
    }

    public boolean deleteFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return true;
        }

        return false;
    }

    public boolean isValidImageFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }

        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        return extension.matches("\\.(jpg|jpeg|png|gif)$");
    }
}