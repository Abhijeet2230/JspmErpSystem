package in.edu.jspmjscoe.admissionportal.services.impl.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import in.edu.jspmjscoe.admissionportal.exception.minio.AchievementFileAccessException;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    /**
     * Upload a file to MinIO
     */
    public String uploadFile(MultipartFile file, String objectName) {
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return objectName;
        } catch (Exception e) {
            throw new AchievementFileAccessException(
                    "Failed to upload file to MinIO: " + file.getOriginalFilename(), e
            );
        }
    }

    /**
     * Generate structured object key
     */
    public String generateObjectKey(String type, Long studentAcademicYearId, String filename) {
        return type + "/student_" + studentAcademicYearId + "/" + filename;
    }

    /**
     * Stream file from MinIO
     */
    public InputStreamResource getFile(String objectKey) {
        validateObjectKey(objectKey);
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .build()
            );
            return new InputStreamResource(inputStream);
        } catch (Exception e) {
            throw new AchievementFileAccessException(
                    "Failed to fetch file from MinIO: " + objectKey, e
            );
        }
    }

    /**
     * Basic object key validation
     */
    private void validateObjectKey(String objectKey) {
        if (objectKey.contains("..") || objectKey.startsWith("/")) {
            throw new AchievementFileAccessException("Invalid object key: " + objectKey);
        }
    }

    /**
     * Uploads a student's profile picture to MinIO and returns the object key/path
     */
    public String uploadStudentProfilePic(MultipartFile file, Long studentId) {
        // Create a structured object key for student profile
        String objectKey = "student-profiles/student_" + studentId + "/" + file.getOriginalFilename();

        // Use existing generic upload method
        return uploadFile(file, objectKey);
    }

}
