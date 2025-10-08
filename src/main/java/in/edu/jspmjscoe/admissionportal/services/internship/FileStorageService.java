package in.edu.jspmjscoe.admissionportal.services.internship;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Service interface for file storage operations using MinIO
 */
public interface FileStorageService {
    
    /**
     * Upload a file to storage
     * @param file The file to upload
     * @param bucketName The bucket to store the file in
     * @param objectName The name/path for the stored file
     * @return The URL or path to access the uploaded file
     */
    String uploadFile(MultipartFile file, String bucketName, String objectName);
    
    /**
     * Download a file from storage
     * @param bucketName The bucket containing the file
     * @param objectName The name/path of the file
     * @return InputStream of the file content
     */
    InputStream downloadFile(String bucketName, String objectName);
    
    /**
     * Delete a file from storage
     * @param bucketName The bucket containing the file
     * @param objectName The name/path of the file
     */
    void deleteFile(String bucketName, String objectName);
    
    /**
     * Check if a file exists in storage
     * @param bucketName The bucket to check
     * @param objectName The name/path of the file
     * @return true if file exists, false otherwise
     */
    boolean fileExists(String bucketName, String objectName);
    
    /**
     * Get the URL to access a file
     * @param bucketName The bucket containing the file
     * @param objectName The name/path of the file
     * @return The URL to access the file
     */
    String getFileUrl(String bucketName, String objectName);
    
    /**
     * Validate file type and size
     * @param file The file to validate
     * @param allowedTypes Array of allowed MIME types
     * @param maxSizeBytes Maximum file size in bytes
     * @return true if valid, false otherwise
     */
    boolean validateFile(MultipartFile file, String[] allowedTypes, long maxSizeBytes);
}