package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.exception.minio.MinioStorageException;
import in.edu.jspmjscoe.admissionportal.services.internship.FileStorageService;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    
    @Value("${minio.url}")
    private String minioUrl;

    @Override
    public String uploadFile(MultipartFile file, String bucketName, String objectName) {
        try {
            // Ensure bucket exists
            createBucketIfNotExists(bucketName);
            
            // Upload file
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            log.info("File uploaded successfully: {}/{}", bucketName, objectName);
            return getFileUrl(bucketName, objectName);
            
        } catch (Exception e) {
            log.error("Error uploading file: {}/{}", bucketName, objectName, e);
            throw new MinioStorageException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            log.error("Error downloading file: {}/{}", bucketName, objectName, e);
            throw new MinioStorageException("Failed to download file: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            log.info("File deleted successfully: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Error deleting file: {}/{}", bucketName, objectName, e);
            throw new MinioStorageException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean fileExists(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new MinioStorageException("Error checking file existence: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new MinioStorageException("Error checking file existence: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String bucketName, String objectName) {
        return String.format("%s/%s/%s", minioUrl, bucketName, objectName);
    }

    @Override
    public boolean validateFile(MultipartFile file, String[] allowedTypes, long maxSizeBytes) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        // Check file size
        if (file.getSize() > maxSizeBytes) {
            log.warn("File size {} exceeds maximum allowed size {}", file.getSize(), maxSizeBytes);
            return false;
        }
        
        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(allowedTypes).contains(contentType)) {
            log.warn("File type {} not allowed. Allowed types: {}", contentType, Arrays.toString(allowedTypes));
            return false;
        }
        
        return true;
    }

    private void createBucketIfNotExists(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
            
            if (!exists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
                );
                log.info("Bucket created: {}", bucketName);
            }
        } catch (Exception e) {
            throw new MinioStorageException("Failed to create bucket: " + e.getMessage(), e);
        }
    }
}