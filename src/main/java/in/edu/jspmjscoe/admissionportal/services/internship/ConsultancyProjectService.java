package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.ConsultancyProjectWorkDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.ProjectStatus;
import java.util.List;
import java.util.Optional;

public interface ConsultancyProjectService {
    
    ConsultancyProjectWorkDTO createProject(ConsultancyProjectWorkDTO projectDTO);
    
    ConsultancyProjectWorkDTO updateProject(Long projectId, ConsultancyProjectWorkDTO projectDTO);
    
    Optional<ConsultancyProjectWorkDTO> getProjectById(Long projectId);
    
    List<ConsultancyProjectWorkDTO> getAllProjects();
    
    List<ConsultancyProjectWorkDTO> getProjectsByOrganization(String organization);
    
    List<ConsultancyProjectWorkDTO> getProjectsByStatus(ProjectStatus status);
    
    List<ConsultancyProjectWorkDTO> getProjectsByTitle(String projectTitle);
    
    List<ConsultancyProjectWorkDTO> getActiveProjects();
    
    void deleteProject(Long projectId);
    
    void updateProjectStatus(Long projectId, ProjectStatus status);
}
