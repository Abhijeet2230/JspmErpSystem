package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.*;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationFormServiceImpl implements ApplicationFormService {

    private final StudentRepository studentRepository;
    private final StudentInternshipProfileService studentInternshipProfileService;
    private final InternshipPostingService internshipPostingService;
    private final PlacementService placementService;
    private final InternshipApplicationService internshipApplicationService;
    private final PlacementApplicationService placementApplicationService;
    private final ResumeService resumeService;

    @Override
    @Transactional(readOnly = true)
    public InternshipApplicationDTO getInternshipApplicationForm(Long studentId, Long internshipId) {
        log.info("Getting internship application form for student {} and internship {}", studentId, internshipId);
        
        // Get student data
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        // Get student profile
        StudentProfileDTO profile = studentInternshipProfileService.getProfileByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for ID: " + studentId));
        
        // Check if internship exists
        internshipPostingService.getPostingById(internshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found with ID: " + internshipId));
        
        // Build form DTO with pre-filled data
        return InternshipApplicationDTO.builder()
                .internshipId(internshipId)
                // Student data
                .prnNumber(student.getPrnNumber())
                .candidateName(student.getCandidateName())
                // Profile data
                .firstName(profile.getFirstName())
                .middleName(profile.getMiddleName())
                .lastName(profile.getLastName())
                .gender(profile.getGender())
                .branch(profile.getBranch())
                .email(profile.getEmail())
                .mobileNumber(profile.getMobileNumber())
                // Academic info
                .currentSemester(profile.getCurrentSemester())
                .currentCgpa(profile.getCurrentCgpa())
                .aggregatePercentage(profile.getAggregatePercentage())
                .deadBacklogs(profile.getDeadBacklogs())
                .liveBacklogs(profile.getLiveBacklogs())
                .clearBacklogsConfidence(profile.getClearBacklogsConfidence())
                // Educational background
                .tenthPercentage(profile.getTenthPercentage())
                .tenthBoard(profile.getTenthBoard())
                .twelfthPercentage(profile.getTwelfthPercentage())
                .twelfthBoard(profile.getTwelfthBoard())
                .diplomaPercentage(profile.getDiplomaPercentage())
                .diplomaBoard(profile.getDiplomaBoard())
                // Career preferences
                .careerInterest(profile.getCareerInterest())
                .higherStudies(profile.getHigherStudies())
                .placementInterest(profile.getPlacementInterest())
                .relocationInterest(profile.getRelocationInterest())
                .bondAcceptance(profile.getBondAcceptance())
                // Skills and documents
                .certifications(profile.getCertifications())
                .resumePath(profile.getResumePath())
                .panCard(profile.getPanCard())
                .aadhaarCard(profile.getAadhaarCard())
                .passport(profile.getPassport())
                // Address
                .localCity(profile.getLocalCity())
                .permanentCity(profile.getPermanentCity())
                .permanentState(profile.getPermanentState())
                // Validation flags
                .hasResume(resumeService.hasResume(studentId))
                .profileComplete(validateStudentProfileForApplication(studentId).isEmpty())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PlacementApplicationDTO getPlacementApplicationForm(Long studentId, Long placementId) {
        log.info("Getting placement application form for student {} and placement {}", studentId, placementId);
        
        // Get student data
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        // Get student profile
        StudentProfileDTO profile = studentInternshipProfileService.getProfileByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for ID: " + studentId));
        
        // Check if placement exists
        placementService.getPlacementById(placementId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement not found with ID: " + placementId));
        
        // Build form DTO with pre-filled data
        return PlacementApplicationDTO.builder()
                .placementId(placementId)
                // Student data
                .prnNumber(student.getPrnNumber())
                .candidateName(student.getCandidateName())
                // Profile data
                .firstName(profile.getFirstName())
                .middleName(profile.getMiddleName())
                .lastName(profile.getLastName())
                .gender(profile.getGender())
                .branch(profile.getBranch())
                .email(profile.getEmail())
                .mobileNumber(profile.getMobileNumber())
                // Academic info
                .currentSemester(profile.getCurrentSemester())
                .currentCgpa(profile.getCurrentCgpa())
                .aggregatePercentage(profile.getAggregatePercentage())
                .deadBacklogs(profile.getDeadBacklogs())
                .liveBacklogs(profile.getLiveBacklogs())
                .clearBacklogsConfidence(profile.getClearBacklogsConfidence())
                // Educational background
                .tenthPercentage(profile.getTenthPercentage())
                .tenthBoard(profile.getTenthBoard())
                .twelfthPercentage(profile.getTwelfthPercentage())
                .twelfthBoard(profile.getTwelfthBoard())
                .diplomaPercentage(profile.getDiplomaPercentage())
                .diplomaBoard(profile.getDiplomaBoard())
                // Career preferences
                .careerInterest(profile.getCareerInterest())
                .higherStudies(profile.getHigherStudies())
                .placementInterest(profile.getPlacementInterest())
                .relocationInterest(profile.getRelocationInterest())
                .bondAcceptance(profile.getBondAcceptance())
                // Skills and documents
                .certifications(profile.getCertifications())
                .resumePath(profile.getResumePath())
                .panCard(profile.getPanCard())
                .aadhaarCard(profile.getAadhaarCard())
                .passport(profile.getPassport())
                // Address
                .localCity(profile.getLocalCity())
                .permanentCity(profile.getPermanentCity())
                .permanentState(profile.getPermanentState())
                // Validation flags
                .hasResume(resumeService.hasResume(studentId))
                .profileComplete(validateStudentProfileForApplication(studentId).isEmpty())
                .build();
    }

    @Override
    public InternshipApplicationDTO submitInternshipApplication(InternshipApplicationDTO applicationDTO) {
        log.info("Submitting internship application for student {} to internship {}", 
                applicationDTO.getStudentId(), applicationDTO.getInternshipId());
        
        // Validate student profile completeness
        List<String> missingFields = validateStudentProfileForApplication(applicationDTO.getStudentId());
        if (!missingFields.isEmpty()) {
            throw new IllegalStateException("Student profile is incomplete. Missing fields: " + String.join(", ", missingFields));
        }
        
        // Submit application through the application service
        return internshipApplicationService.applyForInternship(applicationDTO);
    }

    @Override
    public PlacementApplicationDTO submitPlacementApplication(PlacementApplicationDTO applicationDTO) {
        log.info("Submitting placement application for student {} to placement {}", 
                applicationDTO.getStudentId(), applicationDTO.getPlacementId());
        
        // Validate student profile completeness
        List<String> missingFields = validateStudentProfileForApplication(applicationDTO.getStudentId());
        if (!missingFields.isEmpty()) {
            throw new IllegalStateException("Student profile is incomplete. Missing fields: " + String.join(", ", missingFields));
        }
        
        // Submit application through the application service
        return placementApplicationService.applyForPlacement(applicationDTO);
    }



    @Override
    @Transactional(readOnly = true)
    public List<String> validateStudentProfileForApplication(Long studentId) {
        List<String> missingFields = new ArrayList<>();
        
        try {
            StudentProfileDTO profile = studentInternshipProfileService.getProfileByStudentId(studentId)
                    .orElse(null);
            
            if (profile == null) {
                missingFields.add("Complete Student Profile");
                return missingFields;
            }
            
            // Check required fields
            if (isEmpty(profile.getFirstName())) missingFields.add("First Name");
            if (isEmpty(profile.getLastName())) missingFields.add("Last Name");
            if (isEmpty(profile.getGender())) missingFields.add("Gender");
            if (isEmpty(profile.getBranch())) missingFields.add("Branch");
            if (isEmpty(profile.getEmail())) missingFields.add("Email");
            if (isEmpty(profile.getMobileNumber())) missingFields.add("Mobile Number");
            if (profile.getCurrentSemester() == null) missingFields.add("Current Semester");
            if (profile.getCurrentCgpa() == null) missingFields.add("Current CGPA");
            if (profile.getAggregatePercentage() == null) missingFields.add("Aggregate Percentage");
            if (isEmpty(profile.getDeadBacklogs())) missingFields.add("Dead Backlogs");
            if (isEmpty(profile.getLiveBacklogs())) missingFields.add("Live Backlogs");
            if (isEmpty(profile.getClearBacklogsConfidence())) missingFields.add("Backlogs Confidence");
            if (profile.getTenthPercentage() == null) missingFields.add("10th Percentage");
            if (isEmpty(profile.getTenthBoard())) missingFields.add("10th Board");
            if (isEmpty(profile.getCareerInterest())) missingFields.add("Career Interest");
            if (isEmpty(profile.getPlacementInterest())) missingFields.add("Placement Interest");
            if (isEmpty(profile.getRelocationInterest())) missingFields.add("Relocation Interest");
            if (isEmpty(profile.getBondAcceptance())) missingFields.add("Bond Acceptance");
            if (isEmpty(profile.getPanCard())) missingFields.add("PAN Card");
            if (isEmpty(profile.getAadhaarCard())) missingFields.add("Aadhaar Card");
            if (isEmpty(profile.getLocalCity())) missingFields.add("Local City");
            if (isEmpty(profile.getPermanentCity())) missingFields.add("Permanent City");
            if (isEmpty(profile.getPermanentState())) missingFields.add("Permanent State");
            
            // Check if resume is uploaded
            if (!resumeService.hasResume(studentId)) {
                missingFields.add("Resume Upload");
            }
            
        } catch (Exception e) {
            log.error("Error validating student profile for ID: {}", studentId, e);
            missingFields.add("Profile Validation Error");
        }
        
        return missingFields;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canApplyForInternship(Long studentId, Long internshipId) {
        // Check if already applied
        if (internshipApplicationService.hasStudentAppliedForInternship(studentId, internshipId)) {
            return false;
        }
        
        // Check if profile is complete
        return validateStudentProfileForApplication(studentId).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canApplyForPlacement(Long studentId, Long placementId) {
        // Check if already applied
        if (placementApplicationService.hasStudentAppliedForPlacement(studentId, placementId)) {
            return false;
        }
        
        // Check if profile is complete
        return validateStudentProfileForApplication(studentId).isEmpty();
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}