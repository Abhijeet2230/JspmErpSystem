package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.StudentProfileDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;

import in.edu.jspmjscoe.admissionportal.services.internship.FileStorageService;
import in.edu.jspmjscoe.admissionportal.services.internship.ResumeService;
import in.edu.jspmjscoe.admissionportal.services.internship.StudentInternshipProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {

    private final FileStorageService fileStorageService;
    private final StudentInternshipProfileService studentInternshipProfileService;
    
    @Value("${app.resume.bucket-name:student-resumes}")
    private String resumeBucketName;
    
    @Value("${app.resume.max-size-mb:5}")
    private int maxResumeSizeMB;
    
    private static final String[] ALLOWED_RESUME_TYPES = {
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    };

    @Override
    public String uploadResume(Long studentId, MultipartFile resumeFile) {
        log.info("Uploading resume for student ID: {}", studentId);
        
        // Validate file
        if (!validateResumeFile(resumeFile)) {
            throw new IllegalArgumentException("Invalid resume file. Please upload a PDF or Word document under " + maxResumeSizeMB + "MB.");
        }
        
        // Get or create student profile
        StudentProfileDTO profile = getOrCreateStudentProfile(studentId);
        
        // Delete existing resume if present
        if (profile.getResumePath() != null && !profile.getResumePath().isEmpty()) {
            try {
                deleteExistingResume(profile.getResumePath());
            } catch (Exception e) {
                log.warn("Failed to delete existing resume for student {}: {}", studentId, e.getMessage());
            }
        }
        
        // Generate unique file name
        String fileName = generateResumeFileName(studentId, resumeFile.getOriginalFilename());
        
        // Upload file
        String resumeUrl = fileStorageService.uploadFile(resumeFile, resumeBucketName, fileName);
        
        // Update student profile with resume path
        profile.setResumePath(fileName);
        studentInternshipProfileService.updateProfile(profile.getProfileId(), profile);
        
        log.info("Resume uploaded successfully for student ID: {}, file: {}", studentId, fileName);
        return resumeUrl;
    }

    @Override
    public InputStream downloadResume(Long studentId) {
        log.info("Downloading resume for student ID: {}", studentId);
        
        StudentProfileDTO profile = studentInternshipProfileService.getProfileByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for ID: " + studentId));
        
        if (profile.getResumePath() == null || profile.getResumePath().isEmpty()) {
            throw new ResourceNotFoundException("No resume found for student ID: " + studentId);
        }
        
        return fileStorageService.downloadFile(resumeBucketName, profile.getResumePath());
    }

    @Override
    public void deleteResume(Long studentId) {
        log.info("Deleting resume for student ID: {}", studentId);
        
        StudentProfileDTO profile = studentInternshipProfileService.getProfileByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for ID: " + studentId));
        
        if (profile.getResumePath() != null && !profile.getResumePath().isEmpty()) {
            fileStorageService.deleteFile(resumeBucketName, profile.getResumePath());
            
            // Update profile to remove resume path
            profile.setResumePath(null);
            studentInternshipProfileService.updateProfile(profile.getProfileId(), profile);
            
            log.info("Resume deleted successfully for student ID: {}", studentId);
        }
    }

    @Override
    public boolean hasResume(Long studentId) {
        return studentInternshipProfileService.getProfileByStudentId(studentId)
                .map(profile -> profile.getResumePath() != null && !profile.getResumePath().isEmpty())
                .orElse(false);
    }

    @Override
    public String getResumeUrl(Long studentId) {
        StudentProfileDTO profile = studentInternshipProfileService.getProfileByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for ID: " + studentId));
        
        if (profile.getResumePath() == null || profile.getResumePath().isEmpty()) {
            throw new ResourceNotFoundException("No resume found for student ID: " + studentId);
        }
        
        return fileStorageService.getFileUrl(resumeBucketName, profile.getResumePath());
    }

    @Override
    public boolean validateResumeFile(MultipartFile resumeFile) {
        long maxSizeBytes = maxResumeSizeMB * 1024L * 1024L; // Convert MB to bytes
        return fileStorageService.validateFile(resumeFile, ALLOWED_RESUME_TYPES, maxSizeBytes);
    }

    @Override
    public ResumeFileInfo getResumeFileInfo(Long studentId) {
        StudentProfileDTO profile = studentInternshipProfileService.getProfileByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for ID: " + studentId));
        
        if (profile.getResumePath() == null || profile.getResumePath().isEmpty()) {
            throw new ResourceNotFoundException("No resume found for student ID: " + studentId);
        }
        
        // Extract file info from path (this is a simplified version)
        String fileName = extractOriginalFileName(profile.getResumePath());
        String contentType = determineContentType(fileName);
        String downloadUrl = getResumeUrl(studentId);
        
        return new ResumeFileInfo(
            fileName,
            contentType,
            0L, // File size would need to be stored or retrieved from MinIO
            "N/A", // Upload date would need to be stored in profile
            downloadUrl
        );
    }

    private StudentProfileDTO getOrCreateStudentProfile(Long studentId) {
        return studentInternshipProfileService.getProfileByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Student profile not found for ID: " + studentId + ". Please create a profile first."));
    }

    private String generateResumeFileName(Long studentId, String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFileName);
        
        return String.format("student_%d/resume_%s_%s%s", studentId, timestamp, uniqueId, extension);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ".pdf"; // Default extension
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private void deleteExistingResume(String resumePath) {
        fileStorageService.deleteFile(resumeBucketName, resumePath);
    }

    private String extractOriginalFileName(String resumePath) {
        // Extract filename from path like "student_123/resume_20231201_12345678.pdf"
        if (resumePath.contains("/")) {
            return resumePath.substring(resumePath.lastIndexOf("/") + 1);
        }
        return resumePath;
    }

    private String determineContentType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return switch (extension) {
            case ".pdf" -> "application/pdf";
            case ".doc" -> "application/msword";
            case ".docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default -> "application/octet-stream";
        };
    }
}