package in.edu.jspmjscoe.admissionportal.services.impl.achievements;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import in.edu.jspmjscoe.admissionportal.exception.MinioStorageException;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

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
            throw new MinioStorageException("Failed to upload file to MinIO: " + file.getOriginalFilename(), e);
        }
    }

    public String generateObjectKey(String type, Long studentAcademicYearId, String filename) {
        return type + "/student_" + studentAcademicYearId + "/" + filename;
    }

    public String getPresignedUrl(String objectKey) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectKey)
                            .expiry(60 * 60) // 1 hour
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
    }

}
