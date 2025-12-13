package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public String storeFile(MultipartFile file, String subDirectory) {
        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File không được để trống");
            }

            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;
            Path uploadPath = Paths.get(uploadDir + "/" + subDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + subDirectory + "/" + fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu file: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String storeProductImage(MultipartFile file) {
        return storeFile(file, "products");
    }

    @Override
    public String storeAvatar(MultipartFile file, String userType) {
        return storeFile(file, "avatars/" + userType);
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            if (filePath == null || filePath.isEmpty()) {
                return false;
            }

            String relativePath = filePath.replaceFirst("^/uploads/", "");
            Path fileToDelete = Paths.get(uploadDir).resolve(relativePath);

            return Files.deleteIfExists(fileToDelete);

        } catch (IOException ex) {
            throw new RuntimeException("Không thể xóa file: " + filePath, ex);
        }
    }

    @Override
    public byte[] getFile(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            return new byte[0];
        }

        String relativePath = filePath.replaceFirst("^/uploads/", "");
        Path filePathFull = Paths.get(uploadDir).resolve(relativePath);

        if (Files.exists(filePathFull)) {
            return Files.readAllBytes(filePathFull);
        }

        return new byte[0];
    }

    @Override
    public String getFileUrl(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        return baseUrl + filePath;
    }

    @Override
    public boolean isImageFile(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            return false;
        }

        String fileName = file.getOriginalFilename().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp") || fileName.endsWith(".webp");
    }

    @Override
    public boolean isFileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        try {
            String relativePath = filePath.replaceFirst("^/uploads/", "");
            Path filePathFull = Paths.get(uploadDir).resolve(relativePath);
            return Files.exists(filePathFull);
        } catch (Exception e) {
            return false;
        }
    }
}