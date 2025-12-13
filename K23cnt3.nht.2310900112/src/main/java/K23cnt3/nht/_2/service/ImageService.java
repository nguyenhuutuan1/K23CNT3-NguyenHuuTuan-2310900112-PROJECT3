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
public class ImageService {

    @Value("${app.file.upload-dir:uploads/images}")
    private String uploadDir;

    @Value("classpath:/static/images/")
    private Path staticImagesPath;

    /**
     * Upload ảnh mới
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // Kiểm tra file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Kiểm tra định dạng
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isImageFile(originalFilename)) {
            throw new IllegalArgumentException("Invalid image file");
        }

        // Tạo tên file duy nhất
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Lưu file
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);

        return uniqueFilename;
    }

    /**
     * Lấy URL của ảnh upload
     */
    public String getUploadedImageUrl(String filename) {
        return "/uploaded-images/" + filename;
    }

    /**
     * Lấy URL của ảnh tĩnh (có sẵn)
     */
    public String getStaticImageUrl(String category, String filename) {
        return "/images/" + category + "/" + filename;
    }

    /**
     * Xóa ảnh upload
     */
    public boolean deleteUploadedImage(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return true;
        }
        return false;
    }

    /**
     * Kiểm tra có phải file ảnh không
     */
    private boolean isImageFile(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        return extension.matches("\\.(jpg|jpeg|png|gif)$");
    }

    /**
     * Lấy danh sách ảnh tĩnh theo category
     */
    public String[] getStaticImages(String category) throws IOException {
        Path categoryPath = staticImagesPath.resolve(category);
        if (Files.exists(categoryPath)) {
            return Files.list(categoryPath)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(this::isImageFile)
                    .toArray(String[]::new);
        }
        return new String[0];
    }
}