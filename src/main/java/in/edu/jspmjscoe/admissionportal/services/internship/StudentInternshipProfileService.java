package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.StudentProfileDTO;
import java.util.List;
import java.util.Optional;

public interface StudentInternshipProfileService {
    
    StudentProfileDTO createProfile(StudentProfileDTO profileDTO);
    
    StudentProfileDTO updateProfile(Long profileId, StudentProfileDTO profileDTO);
    
    Optional<StudentProfileDTO> getProfileById(Long profileId);
    
    Optional<StudentProfileDTO> getProfileByStudentId(Long studentId);
    
    List<StudentProfileDTO> getAllProfiles();
    
    void deleteProfile(Long profileId);
    
    boolean existsByStudentId(Long studentId);
    
    boolean existsByEmail(String email);
}
