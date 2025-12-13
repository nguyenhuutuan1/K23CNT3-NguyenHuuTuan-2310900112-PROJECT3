package K23cnt3.nht._2.controller;

import K23cnt3.nht._2.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload/product")
    @ResponseBody
    public ResponseEntity<?> uploadProductImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File không được để trống");
            }

            if (!fileStorageService.isImageFile(file)) {
                return ResponseEntity.badRequest().body("Chỉ chấp nhận file ảnh (JPG, PNG, GIF, BMP, WEBP)");
            }

            String filePath = fileStorageService.storeProductImage(file);

            Map<String, String> response = new HashMap<>();
            response.put("original", fileStorageService.getFileUrl(filePath));
            response.put("fileName", getFileNameFromPath(filePath));
            response.put("message", "Upload ảnh sản phẩm thành công");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi upload: " + e.getMessage());
        }
    }

    @PostMapping("/upload/avatar")
    @ResponseBody
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userType", defaultValue = "default") String userType) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File không được để trống");
            }

            if (!fileStorageService.isImageFile(file)) {
                return ResponseEntity.badRequest().body("Chỉ chấp nhận file ảnh (JPG, PNG, GIF, BMP, WEBP)");
            }

            String filePath = fileStorageService.storeAvatar(file, userType);

            Map<String, String> response = new HashMap<>();
            response.put("url", fileStorageService.getFileUrl(filePath));
            response.put("fileName", getFileNameFromPath(filePath));
            response.put("message", "Upload avatar thành công");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi upload: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteImage(@RequestParam("filePath") String filePath) {
        try {
            boolean deleted = fileStorageService.deleteFile(filePath);

            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Xóa ảnh thành công"));
            } else {
                return ResponseEntity.badRequest().body("Không tìm thấy file để xóa");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xóa ảnh: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<?> checkImageExists(@RequestParam("filePath") String filePath) {
        boolean exists = fileStorageService.isFileExists(filePath);

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("filePath", filePath);

        if (exists) {
            response.put("url", fileStorageService.getFileUrl(filePath));
        }

        return ResponseEntity.ok(response);
    }

    private String getFileNameFromPath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        int lastSlash = filePath.lastIndexOf('/');
        if (lastSlash != -1 && lastSlash < filePath.length() - 1) {
            return filePath.substring(lastSlash + 1);
        }

        return filePath;
    }
}