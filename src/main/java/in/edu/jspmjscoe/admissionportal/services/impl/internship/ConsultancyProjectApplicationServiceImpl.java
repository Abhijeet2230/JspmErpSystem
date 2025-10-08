package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.ConsultancyProjectApplicationDTO;
import in.edu.jspmjscoe.admissionportal.exception.DuplicateResourceException;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.ConsultancyProjectApplicationMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import in.edu.jspmjscoe.admissionportal.model.internship.ConsultancyProjectApplication;
import in.edu.jspmjscoe.admissionportal.repositories.internship.ConsultancyProjectApplicationRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.ConsultancyProjectApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultancyProjectApplicationServiceImpl implements ConsultancyProjectApplicationService {

    private final ConsultancyProjectApplicationRepository applicationRepository;
    private final ConsultancyProjectApplicationMapper applicationMapper;

    @Override
    public ConsultancyProjectApplicationDTO applyForProject(ConsultancyProjectApplicationDTO applicationDTO) {
        // Check if student has already applied for this project
        if (hasStudentAppliedForProject(applicationDTO.getStudentId(), applicationDTO.getProjectId())) {
            throw new DuplicateResourceException("Student has already applied for this project");
        }
        
        ConsultancyProjectApplication application = applicationMapper.toEntity(applicationDTO);
        application.setAppliedDate(LocalDate.now());
        application.setStatus(ApplicationStatus.APPLIED);
        
        ConsultancyProjectApplication savedApplication = applicationRepository.save(application);
        return applicationMapper.toDTO(savedApplication);
    }

    @Override
    public ConsultancyProjectApplicationDTO updateApplication(Long applicationId, ConsultancyProjectApplicationDTO applicationDTO) {
        ConsultancyProjectApplication existingApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        
        // Update fields
        existingApplication.setStatus(applicationDTO.getStatus());
        existingApplication.setSelectedDate(applicationDTO.getSelectedDate());
        existingApplication.setRejectedDate(applicationDTO.getRejectedDate());
        existingApplication.setRemarks(applicationDTO.getRemarks());
        
        ConsultancyProjectApplication updatedApplication = applicationRepository.save(existingApplication);
        return applicationMapper.toDTO(updatedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsultancyProjectApplicationDTO> getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .map(applicationMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectApplicationDTO> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentStudentId(studentId)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectApplicationDTO> getApplicationsByProject(Long projectId) {
        return applicationRepository.findByProjectId(projectId)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectApplicationDTO> getApplicationsByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultancyProjectApplicationDTO> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteApplication(Long applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Application not found with id: " + applicationId);
        }
        applicationRepository.deleteById(applicationId);
    }

    @Override
    public void updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        ConsultancyProjectApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        
        application.setStatus(status);
        
        // Set appropriate date based on status
        if (status == ApplicationStatus.SELECTED) {
            application.setSelectedDate(LocalDate.now());
        } else if (status == ApplicationStatus.REJECTED) {
            application.setRejectedDate(LocalDate.now());
        }
        
        applicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasStudentAppliedForProject(Long studentId, Long projectId) {
        return applicationRepository.existsByStudentStudentIdAndProjectId(studentId, projectId);
    }

    @Override
    public void withdrawApplication(Long applicationId) {
        ConsultancyProjectApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        
        if (application.getStatus() == ApplicationStatus.APPLIED) {
            applicationRepository.delete(application);
        } else {
            throw new IllegalStateException("Cannot withdraw application that is not in APPLIED status");
        }
    }
}
