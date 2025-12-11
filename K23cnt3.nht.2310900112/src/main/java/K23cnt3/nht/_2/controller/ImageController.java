package K23cnt3.nht._2.controller;

import K23cnt3.nht._2.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Upload ảnh sản phẩm
     */
    @PostMapping("/upload/product")
    public ResponseEntity<?> uploadProductImage(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileStorageService.storeProductImage(file);

            // Tạo thumbnail
            String thumbnailPath = fileStorageService.createThumbnail(filePath, 300, 300);

            Map<String, String> response = new HashMap<>();
            response.put("original", fileStorageService.getFileUrl(filePath));
            response.put("thumbnail", fileStorageService.getFileUrl(thumbnailPath));
            response.put("fileName", getFileNameFromPath(filePath));

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }

    /**
     * Upload avatar
     */
    @PostMapping("/upload/avatar")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userType", defaultValue = "default") String userType) {

        try {
            String filePath = fileStorageService.storeAvatar(file, userType);

            Map<String, String> response = new HashMap<>();
            response.put("url", fileStorageService.getFileUrl(filePath));
            response.put("fileName", getFileNameFromPath(filePath));

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Xóa ảnh
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam String filePath) {
        boolean deleted = fileStorageService.deleteFile(filePath);

        if (deleted) {
            return ResponseEntity.ok().body("Xóa file thành công");
        } else {
            return ResponseEntity.badRequest().body("Không thể xóa file");
        }
    }

    /**
     * Lấy danh sách ảnh sản phẩm
     */
    @GetMapping("/products")
    public ResponseEntity<?> getProductImages(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Implement pagination logic here
        Map<String, Object> response = new HashMap<>();
        response.put("page", page);
        response.put("size", size);
        response.put("images", new String[0]); // Placeholder

        return ResponseEntity.ok(response);
    }

    private String getFileNameFromPath(String filePath) {
        if (filePath == null) return "";
        int lastSlash = filePath.lastIndexOf('/');
        return lastSlash >= 0 ? filePath.substring(lastSlash + 1) : filePath;
    }
}