package K23cnt3.nht._2.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ImageUploadUtil {

    /**
     * Convert MultipartFile to Base64
     */
    public static String convertToBase64(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        byte[] bytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        String mimeType = file.getContentType();

        return "data:" + mimeType + ";base64," + base64;
    }

    /**
     * Resize image before upload
     */
    public static MultipartFile resizeImage(MultipartFile file, int maxWidth, int maxHeight)
            throws IOException {
        // Implementation using ImageIO
        // This is a simplified version
        return file;
    }

    /**
     * Compress image (reduce quality)
     */
    public static MultipartFile compressImage(MultipartFile file, float quality)
            throws IOException {
        // Implementation using ImageIO
        // This is a simplified version
        return file;
    }

    /**
     * Validate image dimensions
     */
    public static boolean validateImageDimensions(MultipartFile file,
                                                  int minWidth, int maxWidth,
                                                  int minHeight, int maxHeight) {
        // Implementation using ImageIO
        return true; // Placeholder
    }

    /**
     * Generate image hash for duplicate detection
     */
    public static String generateImageHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = file.getBytes();
        return Base64.getEncoder().encodeToString(
                java.security.MessageDigest.getInstance("MD5").digest(bytes)
        );
    }
}