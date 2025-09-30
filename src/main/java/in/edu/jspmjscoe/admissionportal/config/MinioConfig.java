package in.edu.jspmjscoe.admissionportal.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    // ---------------- Teacher Appraisal MinIO ----------------
    @Value("${minio.url-my}")
    private String myUrl;
    @Value("${minio.access-key-my}")
    private String myAccessKey;
    @Value("${minio.secret-key-my}")
    private String mySecretKey;

    // ---------------- Student Docs MinIO ----------------
    @Value("${minio.url}")
    private String studentUrl;
    @Value("${minio.access-key}")
    private String studentAccessKey;
    @Value("${minio.secret-key}")
    private String studentSecretKey;

    @Bean(name = "myMinioClient")
    public MinioClient myMinioClient() {
        return MinioClient.builder()
                .endpoint(myUrl)
                .credentials(myAccessKey, mySecretKey)
                .build();
    }

    @Bean(name = "friendMinioClient")
    public MinioClient friendMinioClient() {
        return MinioClient.builder()
                .endpoint(studentUrl)
                .credentials(studentAccessKey, studentSecretKey)
                .build();
    }

}
