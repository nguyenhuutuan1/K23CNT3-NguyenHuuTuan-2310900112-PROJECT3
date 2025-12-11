package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.image.max-size:5242880}") // 5MB
    private long maxFileSize;

    @Value("${app.image.allowed-types:image/jpeg,image/png,image/gif,image/webp}")
    private String[] allowedTypes;

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            // Tạo các thư mục con
            Files.createDirectories(this.fileStorageLocation.resolve("products"));
            Files.createDirectories(this.fileStorageLocation.resolve("products/thumbnails"));
            Files.createDirectories(this.fileStorageLocation.resolve("avatars/customers"));
            Files.createDirectories(this.fileStorageLocation.resolve("avatars/employees"));
            Files.createDirectories(this.fileStorageLocation.resolve("avatars/default"));
            Files.createDirectories(this.fileStorageLocation.resolve("banners"));
            Files.createDirectories(this.fileStorageLocation.resolve("documents"));
            Files.createDirectories(this.fileStorageLocation.resolve("temp"));
        } catch (IOException ex) {
            throw new RuntimeException("Không thể tạo thư mục upload", ex);
        }
    }

    @Override
    public String storeProductImage(MultipartFile file) throws IOException {
        return storeFile(file, "products/" + getCurrentYearMonth());
    }

    @Override
    public String storeAvatar(MultipartFile file, String userType) throws IOException {
        String subDir = "avatars/" + (userType != null ? userType : "default");
        return storeFile(file, subDir);
    }

    @Override
    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        // Validate file
        validateFile(file);

        // Tạo tên file duy nhất
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String fileName = generateUniqueFileName(fileExtension);

        // Tạo thư mục đích nếu chưa tồn tại
        Path targetDir = this.fileStorageLocation.resolve(subDirectory);
        Files.createDirectories(targetDir);

        // Lưu file
        Path targetLocation = targetDir.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        // Trả về đường dẫn tương đối
        return subDirectory + "/" + fileName;
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            Path path = this.fileStorageLocation.resolve(filePath).normalize();
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean fileExists(String filePath) {
        Path path = this.fileStorageLocation.resolve(filePath).normalize();
        return Files.exists(path);
    }

    @Override
    public Path getFullPath(String relativePath) {
        return this.fileStorageLocation.resolve(relativePath).normalize();
    }

    @Override
    public String createThumbnail(String originalPath, int width, int height) throws IOException {
        Path originalFullPath = getFullPath(originalPath);

        if (!Files.exists(originalFullPath)) {
            throw new IOException("File gốc không tồn tại: " + originalPath);
        }

        // Đọc ảnh gốc
        BufferedImage originalImage = ImageIO.read(originalFullPath.toFile());

        // Tính toán kích thước thumbnail giữ tỉ lệ
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Tính tỉ lệ scale
        double widthRatio = (double) width / originalWidth;
        double heightRatio = (double) height / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (originalWidth * ratio);
        int scaledHeight = (int) (originalHeight * ratio);

        // Tạo thumbnail
        BufferedImage thumbnail = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = thumbnail.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // Tạo tên file thumbnail
        String fileExtension = getFileExtension(originalPath);
        String thumbnailName = getFileNameWithoutExtension(originalPath) +
                "_thumb_" + width + "x" + height + fileExtension;

        // Lưu thumbnail
        Path thumbnailDir = this.fileStorageLocation.resolve("products/thumbnails");
        Files.createDirectories(thumbnailDir);

        Path thumbnailPath = thumbnailDir.resolve(thumbnailName);
        ImageIO.write(thumbnail, getFormatName(fileExtension), thumbnailPath.toFile());

        return "products/thumbnails/" + thumbnailName;
    }

    @Override
    public String getFileUrl(String relativePath) {
        return "/uploads/" + relativePath;
    }

    // ===== PRIVATE HELPER METHODS =====

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File trống");
        }

        if (file.getSize() > maxFileSize) {
            throw new IOException("File quá lớn. Kích thước tối đa: " +
                    (maxFileSize / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        String originalFileName = file.getOriginalFilename();

        if (contentType == null || originalFileName == null) {
            throw new IOException("File không hợp lệ");
        }

        // Kiểm tra loại file
        boolean isValidType = false;
        String fileExtension = getFileExtension(originalFileName).toLowerCase();

        for (String allowedType : allowedTypes) {
            if (allowedType.startsWith(".")) {
                // Kiểm tra extension
                if (fileExtension.equals(allowedType.toLowerCase())) {
                    isValidType = true;
                    break;
                }
            } else {
                // Kiểm tra MIME type
                if (contentType.startsWith(allowedType.replace("/*", "/"))) {
                    isValidType = true;
                    break;
                }
            }
        }

        if (!isValidType) {
            throw new IOException("Loại file không được hỗ trợ. Chỉ chấp nhận: " +
                    String.join(", ", allowedTypes));
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex > 0) ? fileName.substring(lastDotIndex) : "";
    }

    private String getFileNameWithoutExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex > 0) ? fileName.substring(0, lastDotIndex) : fileName;
    }

    private String generateUniqueFileName(String extension) {
        return UUID.randomUUID().toString() + extension;
    }

    private String getCurrentYearMonth() {
        LocalDate now = LocalDate.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy/MM"));
    }

    private String getFormatName(String extension) {
        if (extension == null) return "jpg";

        switch (extension.toLowerCase()) {
            case ".jpg":
            case ".jpeg":
                return "jpg";
            case ".png":
                return "png";
            case ".gif":
                return "gif";
            case ".webp":
                return "webp";
            default:
                return "jpg";
        }
    }
}