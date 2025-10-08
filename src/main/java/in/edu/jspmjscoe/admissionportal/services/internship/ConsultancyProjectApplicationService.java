package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.ConsultancyProjectApplicationDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import java.util.List;
import java.util.Optional;

public interface ConsultancyProjectApplicationService {
    
    ConsultancyProjectApplicationDTO applyForProject(ConsultancyProjectApplicationDTO applicationDTO);
    
    ConsultancyProjectApplicationDTO updateApplication(Long applicationId, ConsultancyProjectApplicationDTO applicationDTO);
    
    Optional<ConsultancyProjectApplicationDTO> getApplicationById(Long applicationId);
    
    List<ConsultancyProjectApplicationDTO> getApplicationsByStudent(Long studentId);
    
    List<ConsultancyProjectApplicationDTO> getApplicationsByProject(Long projectId);
    
    List<ConsultancyProjectApplicationDTO> getApplicationsByStatus(ApplicationStatus status);
    
    List<ConsultancyProjectApplicationDTO> getAllApplications();
    
    void deleteApplication(Long applicationId);
    
    void updateApplicationStatus(Long applicationId, ApplicationStatus status);
    
    boolean hasStudentAppliedForProject(Long studentId, Long projectId);
    
    void withdrawApplication(Long applicationId);
}
