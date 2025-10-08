package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementApplicationDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import java.util.List;
import java.util.Optional;

public interface PlacementApplicationService {
    
    PlacementApplicationDTO applyForPlacement(PlacementApplicationDTO applicationDTO);
    
    PlacementApplicationDTO updateApplication(Long applicationId, PlacementApplicationDTO applicationDTO);
    
    Optional<PlacementApplicationDTO> getApplicationById(Long applicationId);
    
    List<PlacementApplicationDTO> getApplicationsByStudent(Long studentId);
    
    List<PlacementApplicationDTO> getApplicationsByPlacement(Long placementId);
    
    List<PlacementApplicationDTO> getApplicationsByStatus(ApplicationStatus status);
    
    List<PlacementApplicationDTO> getAllApplications();
    
    void deleteApplication(Long applicationId);
    
    void updateApplicationStatus(Long applicationId, ApplicationStatus status);
    
    boolean hasStudentAppliedForPlacement(Long studentId, Long placementId);
    
    void withdrawApplication(Long applicationId);
}
