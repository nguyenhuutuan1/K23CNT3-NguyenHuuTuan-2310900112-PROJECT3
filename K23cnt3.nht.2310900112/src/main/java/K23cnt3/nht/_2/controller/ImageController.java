package K23cnt3.nht._2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/images")
public class ImageController {

    @Value("${app.file.upload-dir:src/main/resources/static/images}")
    private String uploadDir;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();

        try {
            // Kiểm tra file rỗng
            if (file.isEmpty()) {
                response.put("error", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            // Kiểm tra định dạng
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                response.put("error", "Invalid file name");
                return ResponseEntity.badRequest().body(response);
            }

            String extension = "";
            if (originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            }

            if (!extension.matches("\\.(jpg|jpeg|png|gif)$")) {
                response.put("error", "Only JPG, JPEG, PNG, GIF files are allowed");
                return ResponseEntity.badRequest().body(response);
            }

            // Tạo tên file duy nhất
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Path uploadPath = Paths.get(uploadDir);

            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lưu file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // Trả về response
            response.put("filename", uniqueFilename);
            response.put("url", "/images/" + uniqueFilename);
            response.put("message", "File uploaded successfully");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                String contentType = determineContentType(filename);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

    @DeleteMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable String filename) {
        Map<String, String> response = new HashMap<>();

        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                response.put("message", "File deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "File not found");
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            response.put("error", "Failed to delete file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listImages() {
        Map<String, Object> response = new HashMap<>();

        try {
            Path uploadPath = Paths.get(uploadDir);

            if (Files.exists(uploadPath)) {
                var files = Files.list(uploadPath)
                        .filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .filter(name -> name.matches(".*\\.(jpg|jpeg|png|gif)$"))
                        .toList();

                response.put("files", files);
                response.put("count", files.size());
                response.put("directory", uploadDir);

                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Upload directory does not exist");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (IOException e) {
            response.put("error", "Failed to list files: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}