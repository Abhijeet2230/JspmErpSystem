package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipApplicationDTO;
import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementApplicationDTO;

import java.util.List;

/**
 * Service for managing comprehensive application forms with pre-filling and validation
 */
public interface ApplicationFormService {
    
    /**
     * Get pre-filled internship application form for a student
     * @param studentId The student ID
     * @param internshipId The internship ID
     * @return Pre-filled form with student data
     */
    InternshipApplicationDTO getInternshipApplicationForm(Long studentId, Long internshipId);
    
    /**
     * Get pre-filled placement application form for a student
     * @param studentId The student ID
     * @param placementId The placement ID
     * @return Pre-filled form with student data
     */
    PlacementApplicationDTO getPlacementApplicationForm(Long studentId, Long placementId);
    
    /**
     * Submit internship application with comprehensive form data
     * @param formDTO Complete application form data
     * @return Created application DTO
     */
    InternshipApplicationDTO submitInternshipApplication(InternshipApplicationDTO formDTO);
    
    /**
     * Submit placement application with comprehensive form data
     * @param formDTO Complete application form data
     * @return Created application DTO
     */
    PlacementApplicationDTO submitPlacementApplication(PlacementApplicationDTO formDTO);
    
    /**
     * Validate if student profile is complete for application
     * @param studentId The student ID
     * @return List of missing required fields
     */
    List<String> validateStudentProfileForApplication(Long studentId);
    
    /**
     * Check if student can apply for internship
     * @param studentId The student ID
     * @param internshipId The internship ID
     * @return true if can apply, false otherwise
     */
    boolean canApplyForInternship(Long studentId, Long internshipId);
    
    /**
     * Check if student can apply for placement
     * @param studentId The student ID
     * @param placementId The placement ID
     * @return true if can apply, false otherwise
     */
    boolean canApplyForPlacement(Long studentId, Long placementId);
}