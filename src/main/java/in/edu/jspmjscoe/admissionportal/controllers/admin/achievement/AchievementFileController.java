package in.edu.jspmjscoe.admissionportal.controllers.admin.achievement;

import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class AchievementFileController {

    private final MinioStorageService minioService;

    @GetMapping("/{*filePath}")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable String filePath) {
        if (filePath.contains("..")) {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }

        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1); // strip leading slash
        }

        InputStreamResource resource = minioService.getFile(filePath);
        String fileName = Paths.get(filePath).getFileName().toString();
        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        if (fileName.endsWith(".pdf")) contentType = MediaType.APPLICATION_PDF;
        else if (fileName.endsWith(".png")) contentType = MediaType.IMAGE_PNG;
        else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) contentType = MediaType.IMAGE_JPEG;
        else if (fileName.endsWith(".mp4")) contentType = MediaType.APPLICATION_OCTET_STREAM;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
                .contentType(contentType)
                .body(resource);
    }
}