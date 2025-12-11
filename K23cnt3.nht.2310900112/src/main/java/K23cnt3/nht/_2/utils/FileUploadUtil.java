package K23cnt3.nht._2.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtil {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // Upload file và trả về tên file
    public static String uploadFile(MultipartFile file, String subDirectory) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Kiểm tra kích thước file
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File quá lớn. Kích thước tối đa là 5MB.");
        }

        // Kiểm tra định dạng file
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IOException("Tên file không hợp lệ.");
        }

        String fileExtension = getFileExtension(originalFileName);
        if (!isAllowedExtension(fileExtension)) {
            throw new IOException("Định dạng file không được hỗ trợ. Chỉ chấp nhận: " +
                    String.join(", ", ALLOWED_EXTENSIONS));
        }

        // Tạo tên file mới để tránh trùng lặp
        String newFileName = generateFileName(fileExtension);

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(UPLOAD_DIR + subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Lưu file
        Path filePath = uploadPath.resolve(newFileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Trả về đường dẫn tương đối cho web
        return subDirectory + "/" + newFileName;
    }

    // Xóa file
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        try {
            Path path = Paths.get(UPLOAD_DIR + filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    // Lấy extension từ tên file
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex).toLowerCase();
        }
        return "";
    }

    // Kiểm tra extension có hợp lệ không
    private static boolean isAllowedExtension(String extension) {
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    // Tạo tên file mới
    private static String generateFileName(String extension) {
        return UUID.randomUUID().toString() + extension;
    }

    // Lấy đường dẫn đầy đủ
    public static String getFullPath(String relativePath) {
        return UPLOAD_DIR + relativePath;
    }

    // Kiểm tra file có tồn tại không
    public static boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        Path path = Paths.get(UPLOAD_DIR + filePath);
        return Files.exists(path);
    }
}