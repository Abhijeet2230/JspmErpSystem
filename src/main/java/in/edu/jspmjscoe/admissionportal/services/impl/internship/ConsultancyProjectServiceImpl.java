package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.ConsultancyProjectWorkDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.ConsultancyProjectWorkMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.ConsultancyProjectWork;
import in.edu.jspmjscoe.admissionportal.model.internship.ProjectStatus;
import in.edu.jspmjscoe.admissionportal.repositories.internship.ConsultancyProjectWorkRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.ConsultancyProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultancyProjectServiceImpl implements ConsultancyProjectService {

    private final ConsultancyProjectWorkRepository projectRepository;
    private final ConsultancyProjectWorkMapper projectMapper;

    @Override
    public ConsultancyProjectWorkDTO createProject(ConsultancyProjectWorkDTO projectDTO) {
        ConsultancyProjectWork project = projectMapper.toEntity(projectDTO);
        ConsultancyProjectWork savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    @Override
    public ConsultancyProjectWorkDTO updateProject(Long projectId, ConsultancyProjectWorkDTO projectDTO) {
        ConsultancyProjectWork existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultancy project not found with id: " + projectId));
        
        // Update fields
        existingProject.setProjectTitle(projectDTO.getProjectTitle());
        existingProject.setOrganization(projectDTO.getOrganization());
        existingProject.setLocation(projectDTO.getLocation());
        existingProject.setProjectDescription(projectDTO.getProjectDescription());
        existingProject.setRole(projectDTO.getRole());
        existingProject.setStartDate(projectDTO.getStartDate());
        existingProject.setEndDate(projectDTO.getEndDate());
        existingProject.setCertificateProvided(projectDTO.getCertificateProvided());
        existingProject.setStatus(projectDTO.getStatus());
        
        ConsultancyProjectWork updatedProject = projectRepository.save(existingProject);
        return projectMapper.toDTO(updatedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsultancyProjectWorkDTO> getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .map(projectMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectWorkDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectWorkDTO> getProjectsByOrganization(String organization) {
        return projectRepository.findByOrganizationContainingIgnoreCase(organization)
                .stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectWorkDTO> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status)
                .stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectWorkDTO> getProjectsByTitle(String projectTitle) {
        return projectRepository.findByProjectTitleContainingIgnoreCase(projectTitle)
                .stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectWorkDTO> getActiveProjects() {
        return projectRepository.findByStatus(ProjectStatus.ACTIVE)
                .stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Consultancy project not found with id: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }

    @Override
    public void updateProjectStatus(Long projectId, ProjectStatus status) {
        ConsultancyProjectWork project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultancy project not found with id: " + projectId));
        project.setStatus(status);
        projectRepository.save(project);
    }
}
