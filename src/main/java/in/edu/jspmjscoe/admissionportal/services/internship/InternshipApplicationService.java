package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipApplicationDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import java.util.List;
import java.util.Optional;

public interface InternshipApplicationService {
    
    InternshipApplicationDTO applyForInternship(InternshipApplicationDTO applicationDTO);
    
    InternshipApplicationDTO updateApplication(Long applicationId, InternshipApplicationDTO applicationDTO);
    
    Optional<InternshipApplicationDTO> getApplicationById(Long applicationId);
    
    List<InternshipApplicationDTO> getApplicationsByStudentProfile(Long profileId);
    
    List<InternshipApplicationDTO> getApplicationsByInternship(Long internshipId);
    
    List<InternshipApplicationDTO> getApplicationsByStatus(ApplicationStatus status);
    
    List<InternshipApplicationDTO> getAllApplications();
    
    void deleteApplication(Long applicationId);
    
    void updateApplicationStatus(Long applicationId, ApplicationStatus status);
    
    boolean hasProfileAppliedForInternship(Long profileId, Long internshipId);
    
    void withdrawApplication(Long applicationId);
}
