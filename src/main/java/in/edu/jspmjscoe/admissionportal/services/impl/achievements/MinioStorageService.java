package in.edu.jspmjscoe.admissionportal.services.impl.achievements;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient myMinioClient;     // Teacher appraisal bucket
    private final MinioClient friendMinioClient; // Friend/student bucket

    // -------------------- Teacher Appraisal --------------------
    @Value("${minio.bucket-my}")
    private String myBucket;

    public String uploadTeacherAppraisalFile(MultipartFile file, String objectKey) {
        return uploadFileToMinio(myMinioClient, myBucket, file, objectKey);
    }

    public String getPresignedUrlTeacher(String objectKey) {
        return generatePresignedUrl(myMinioClient, myBucket, objectKey);
    }

    // -------------------- Friend/Student Bucket --------------------
    @Value("${minio.bucket}")
    private String friendBucket;  // Keep same property as before for friend

    // Existing friend/student methods (do NOT rename)
    public String uploadFile(MultipartFile file, String objectKey) {
        return uploadFileToMinio(friendMinioClient, friendBucket, file, objectKey);
    }

    public String getPresignedUrl(String objectKey) {
        return generatePresignedUrl(friendMinioClient, friendBucket, objectKey);
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
            throw new RuntimeException("Failed to upload file to MinIO: " + file.getOriginalFilename(), e);
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
            throw new RuntimeException("Failed to generate presigned URL for " + objectKey, e);
        }
    }

    // -------------------- Object Key Generator --------------------
    public String generateObjectKey(String type, Long entityId, String filename) {
        return type + "/" + entityId + "/" + filename;
    }
}
