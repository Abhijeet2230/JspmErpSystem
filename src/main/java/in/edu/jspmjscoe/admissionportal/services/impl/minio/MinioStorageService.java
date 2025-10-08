package in.edu.jspmjscoe.admissionportal.services.impl.minio;

import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
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

    private final MinioClient myMinioClient;     // Teacher appraisal bucket
    private final MinioClient friendMinioClient; // Friend/student bucket

    // -------------------- Teacher Appraisal --------------------
    @Value("${minio.bucket-teacher}")
    private String myBucket;

    public String uploadTeacherAppraisalFile(MultipartFile file, String objectKey) {
        return uploadFileToMinio(myMinioClient, myBucket, file, objectKey);
    }

    public String getPresignedUrlTeacher(String objectKey) {
        return generatePresignedUrl(myMinioClient, myBucket, objectKey);
    }

    // -------------------- Friend/Student Bucket --------------------
    @Value("${minio.bucket}")
    private String friendBucket;

    public String uploadFile(MultipartFile file, String objectKey) {
        return uploadFileToMinio(friendMinioClient, friendBucket, file, objectKey);
    }

    public String getPresignedUrl(String objectKey) {
        return generatePresignedUrl(friendMinioClient, friendBucket, objectKey);
    }

    /**
     * Stream file from Friend/Student MinIO bucket
     */
    public InputStreamResource getFile(String objectKey) {
        validateObjectKey(objectKey);
        try {
            InputStream inputStream = friendMinioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(friendBucket)
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

    // -------------------- Shared Helpers --------------------
    private String uploadFileToMinio(MinioClient client, String bucket, MultipartFile file, String objectKey) {
        try (InputStream is = file.getInputStream()) {
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return objectKey;
        } catch (Exception e) {
            throw new AchievementFileAccessException(
                    "Failed to upload file to MinIO: " + file.getOriginalFilename(), e
            );
        }
    }

    private String generatePresignedUrl(MinioClient client, String bucket, String objectKey) {
        try {
            return client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry(60 * 60) // 1 hour
                            .build()
            );
        } catch (Exception e) {
            throw new AchievementFileAccessException(
                    "Failed to generate presigned URL for " + objectKey, e
            );
        }
    }

    // -------------------- Object Key Utilities --------------------
    public String generateObjectKey(String type, Long entityId, String filename) {
        // Preserves friend's structured format for students
        return type + "/student_" + entityId + "/" + filename;
    }

    private void validateObjectKey(String objectKey) {
        if (objectKey.contains("..") || objectKey.startsWith("/")) {
            throw new AchievementFileAccessException("Invalid object key: " + objectKey);
        }
    }

    public String uploadStudentProfilePic(MultipartFile file, Long studentId) {
        String objectKey = "student-profiles/student_" + studentId + "/" + file.getOriginalFilename();
        return uploadFile(file, objectKey);
    }
}
