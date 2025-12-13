package K23cnt3.nht._2.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile file, String subDirectory);
    String storeProductImage(MultipartFile file);
    String storeAvatar(MultipartFile file, String userType);
    boolean deleteFile(String filePath);
    byte[] getFile(String filePath) throws IOException;
    String getFileUrl(String filePath);
    boolean isImageFile(MultipartFile file);
    boolean isFileExists(String filePath);
}