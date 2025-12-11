package K23cnt3.nht._2.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {

    /**
     * Lưu file sản phẩm
     */
    String storeProductImage(MultipartFile file) throws IOException;

    /**
     * Lưu avatar
     */
    String storeAvatar(MultipartFile file, String userType) throws IOException;

    /**
     * Lưu file vào thư mục cụ thể
     */
    String storeFile(MultipartFile file, String subDirectory) throws IOException;

    /**
     * Xóa file
     */
    boolean deleteFile(String filePath);

    /**
     * Kiểm tra file có tồn tại
     */
    boolean fileExists(String filePath);

    /**
     * Lấy đường dẫn đầy đủ
     */
    Path getFullPath(String relativePath);

    /**
     * Tạo thumbnail từ ảnh
     */
    String createThumbnail(String originalPath, int width, int height) throws IOException;

    /**
     * Lấy URL để truy cập file
     */
    String getFileUrl(String relativePath);
}