package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.StudentProfileDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.StudentInternshipProfileMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.StudentProfile;
import in.edu.jspmjscoe.admissionportal.repositories.internship.StudentProfileRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.StudentInternshipProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentInternshipProfileServiceImpl implements StudentInternshipProfileService {

    private final StudentProfileRepository profileRepository;
    private final StudentInternshipProfileMapper profileMapper;

    @Override
    public StudentProfileDTO createProfile(StudentProfileDTO profileDTO) {
        // Check if profile already exists for this student
        if (existsByStudentId(profileDTO.getStudentId())) {
            throw new IllegalStateException("Profile already exists for this student");
        }
        
        StudentProfile profile = profileMapper.toEntity(profileDTO);
        StudentProfile savedProfile = profileRepository.save(profile);
        return profileMapper.toDTO(savedProfile);
    }

    @Override
    public StudentProfileDTO updateProfile(Long profileId, StudentProfileDTO profileDTO) {
        StudentProfile existingProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found with id: " + profileId));
        
        // Update fields
        existingProfile.setFirstName(profileDTO.getFirstName());
        existingProfile.setMiddleName(profileDTO.getMiddleName());
        existingProfile.setLastName(profileDTO.getLastName());
        existingProfile.setGender(profileDTO.getGender());
        existingProfile.setBranch(profileDTO.getBranch());
        existingProfile.setEmail(profileDTO.getEmail());
        existingProfile.setMobileNumber(profileDTO.getMobileNumber());
        existingProfile.setCurrentSemester(profileDTO.getCurrentSemester());
        existingProfile.setCurrentCgpa(profileDTO.getCurrentCgpa());
        existingProfile.setAggregatePercentage(profileDTO.getAggregatePercentage());
        existingProfile.setDeadBacklogs(profileDTO.getDeadBacklogs());
        existingProfile.setLiveBacklogs(profileDTO.getLiveBacklogs());
        existingProfile.setClearBacklogsConfidence(profileDTO.getClearBacklogsConfidence());
        existingProfile.setTenthPercentage(profileDTO.getTenthPercentage());
        existingProfile.setTenthBoard(profileDTO.getTenthBoard());
        existingProfile.setTwelfthPercentage(profileDTO.getTwelfthPercentage());
        existingProfile.setTwelfthBoard(profileDTO.getTwelfthBoard());
        existingProfile.setDiplomaPercentage(profileDTO.getDiplomaPercentage());
        existingProfile.setDiplomaBoard(profileDTO.getDiplomaBoard());
        existingProfile.setCareerInterest(profileDTO.getCareerInterest());
        existingProfile.setHigherStudies(profileDTO.getHigherStudies());
        existingProfile.setPlacementInterest(profileDTO.getPlacementInterest());
        existingProfile.setRelocationInterest(profileDTO.getRelocationInterest());
        existingProfile.setBondAcceptance(profileDTO.getBondAcceptance());
        existingProfile.setCertifications(profileDTO.getCertifications());
        existingProfile.setResumePath(profileDTO.getResumePath());
        existingProfile.setPanCard(profileDTO.getPanCard());
        existingProfile.setAadhaarCard(profileDTO.getAadhaarCard());
        existingProfile.setPassport(profileDTO.getPassport());
        existingProfile.setLocalCity(profileDTO.getLocalCity());
        existingProfile.setPermanentCity(profileDTO.getPermanentCity());
        existingProfile.setPermanentState(profileDTO.getPermanentState());
        
        StudentProfile updatedProfile = profileRepository.save(existingProfile);
        return profileMapper.toDTO(updatedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentProfileDTO> getProfileById(Long profileId) {
        return profileRepository.findById(profileId)
                .map(profileMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentProfileDTO> getProfileByStudentId(Long studentId) {
        return profileRepository.findByStudentStudentId(studentId)
                .map(profileMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentProfileDTO> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(profileMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProfile(Long profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException("Student profile not found with id: " + profileId);
        }
        profileRepository.deleteById(profileId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByStudentId(Long studentId) {
        return profileRepository.existsByStudentStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return profileRepository.existsByEmail(email);
    }
}
