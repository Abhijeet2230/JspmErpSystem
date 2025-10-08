package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipApplicationDTO;
import in.edu.jspmjscoe.admissionportal.exception.DuplicateResourceException;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.InternshipApplicationMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import in.edu.jspmjscoe.admissionportal.model.internship.InternshipApplication;
import in.edu.jspmjscoe.admissionportal.repositories.internship.InternshipApplicationRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.InternshipApplicationService;
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
public class InternshipApplicationServiceImpl implements InternshipApplicationService {

    private final InternshipApplicationRepository applicationRepository;
    private final InternshipApplicationMapper applicationMapper;

    @Override
    public InternshipApplicationDTO applyForInternship(InternshipApplicationDTO applicationDTO) {
        // Check if student has already applied for this internship
        if (hasStudentAppliedForInternship(applicationDTO.getStudentId(), applicationDTO.getInternshipId())) {
            throw new DuplicateResourceException("Student has already applied for this internship");
        }
        
        InternshipApplication application = applicationMapper.toEntity(applicationDTO);
        application.setAppliedDate(LocalDate.now());
        application.setStatus(ApplicationStatus.APPLIED);
        
        InternshipApplication savedApplication = applicationRepository.save(application);
        return applicationMapper.toDTO(savedApplication);
    }

    @Override
    public InternshipApplicationDTO updateApplication(Long applicationId, InternshipApplicationDTO applicationDTO) {
        InternshipApplication existingApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        
        // Update fields
        existingApplication.setStatus(applicationDTO.getStatus());
        existingApplication.setSelectedDate(applicationDTO.getSelectedDate());
        existingApplication.setRejectedDate(applicationDTO.getRejectedDate());
        existingApplication.setRemarks(applicationDTO.getRemarks());
        
        InternshipApplication updatedApplication = applicationRepository.save(existingApplication);
        return applicationMapper.toDTO(updatedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InternshipApplicationDTO> getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .map(applicationMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipApplicationDTO> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentStudentId(studentId)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipApplicationDTO> getApplicationsByInternship(Long internshipId) {
        return applicationRepository.findByInternshipInternshipId(internshipId)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipApplicationDTO> getApplicationsByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status)
                .stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipApplicationDTO> getAllApplications() {
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
        InternshipApplication application = applicationRepository.findById(applicationId)
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
    public boolean hasStudentAppliedForInternship(Long studentId, Long internshipId) {
        return applicationRepository.existsByStudentStudentIdAndInternshipInternshipId(studentId, internshipId);
    }

    @Override
    public void withdrawApplication(Long applicationId) {
        InternshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        
        if (application.getStatus() == ApplicationStatus.APPLIED) {
            applicationRepository.delete(application);
        } else {
            throw new IllegalStateException("Cannot withdraw application that is not in APPLIED status");
        }
    }
}
