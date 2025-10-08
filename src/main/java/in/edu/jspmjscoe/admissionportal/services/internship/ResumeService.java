package in.edu.jspmjscoe.admissionportal.services.internship;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Service interface for resume management operations
 */
public interface ResumeService {
    
    /**
     * Upload a resume for a student
     * @param studentId The ID of the student
     * @param resumeFile The resume file to upload
     * @return The path/URL of the uploaded resume
     */
    String uploadResume(Long studentId, MultipartFile resumeFile);
    
    /**
     * Download a student's resume
     * @param studentId The ID of the student
     * @return InputStream of the resume file
     */
    InputStream downloadResume(Long studentId);
    
    /**
     * Delete a student's resume
     * @param studentId The ID of the student
     */
    void deleteResume(Long studentId);
    
    /**
     * Check if a student has uploaded a resume
     * @param studentId The ID of the student
     * @return true if resume exists, false otherwise
     */
    boolean hasResume(Long studentId);
    
    /**
     * Get the resume URL for a student
     * @param studentId The ID of the student
     * @return The URL to access the resume
     */
    String getResumeUrl(Long studentId);
    
    /**
     * Validate resume file
     * @param resumeFile The resume file to validate
     * @return true if valid, false otherwise
     */
    boolean validateResumeFile(MultipartFile resumeFile);
    
    /**
     * Get resume file info
     * @param studentId The ID of the student
     * @return Resume file information
     */
    ResumeFileInfo getResumeFileInfo(Long studentId);
    
    /**
     * Resume file information DTO
     */
    record ResumeFileInfo(
        String fileName,
        String contentType,
        long fileSize,
        String uploadDate,
        String downloadUrl
    ) {}
}