package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementApplicationDTO;
import in.edu.jspmjscoe.admissionportal.exception.DuplicateResourceException;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.PlacementApplicationMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import in.edu.jspmjscoe.admissionportal.model.internship.PlacementApplication;
import in.edu.jspmjscoe.admissionportal.repositories.internship.PlacementApplicationRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.PlacementApplicationService;
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
public class PlacementApplicationServiceImpl implements PlacementApplicationService {

    private final PlacementApplicationRepository applicationRepository;
    private final PlacementApplicationMapper applicationMapper;

    @Override
    public PlacementApplicationDTO applyForPlacement(PlacementApplicationDTO applicationDTO) {
        // Check if student has already applied for this placement
        if (hasStudentAppliedForPlacement(applicationDTO.getStudentId(), applicationDTO.getPlacementId())) {
            throw new DuplicateResourceException("Student has already applied for this placement");
        }
        
        PlacementApplication application = applicationMapper.toEntity(applicationDTO);
        application.setAppliedDate(LocalDate.now());
        application.setStatus(ApplicationStatus.APPLIED);
        
        PlacementApplication savedApplication = applicationRepository.save(application);
        return applicationMapper.toDTO(savedApplication);
    }

    @Override
    public PlacementApplicationDTO updateApplication(Long applicationId, PlacementApplicationDTO applicationDTO) {
        PlacementApplication existingApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        
        // Update fields
        existingApplication.setStatus(applicationDTO.getStatus());
        existingApplication.setSelectedDate(applicationDTO.getSelectedDate());
        existingApplication.setRejectedDate(applicationDTO.getRejectedDate());
        existingApplication.setRemarks(applicationDTO.getRemarks());
        
        PlacementApplication updatedApplication = applicationRepository.save(existingApplication);
        return applicationMapper.toDTO(updatedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlacementApplicationDTO> getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .map(applicationMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementApplicationDTO> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentStudentId(studentId)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementApplicationDTO> getApplicationsByPlacement(Long placementId) {
        return applicationRepository.findByPlacementPlacementId(placementId)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementApplicationDTO> getApplicationsByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementApplicationDTO> getAllApplications() {
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
        PlacementApplication application = applicationRepository.findById(applicationId)
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
    public boolean hasStudentAppliedForPlacement(Long studentId, Long placementId) {
        return applicationRepository.existsByStudentStudentIdAndPlacementPlacementId(studentId, placementId);
    }

    @Override
    public void withdrawApplication(Long applicationId) {
        PlacementApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        
        if (application.getStatus() == ApplicationStatus.APPLIED) {
            applicationRepository.delete(application);
        } else {
            throw new IllegalStateException("Cannot withdraw application that is not in APPLIED status");
        }
    }
}
