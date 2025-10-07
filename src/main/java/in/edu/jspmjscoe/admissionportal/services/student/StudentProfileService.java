package in.edu.jspmjscoe.admissionportal.services.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileUpdateDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface StudentProfileService {

    /**
     * Update student profile including address, parent contact, and profile picture
     */
    StudentProfileResponseDTO updateProfile(Long studentId, StudentProfileUpdateDTO dto, MultipartFile profilePicture);

    /**
     * Fetch student profile
     */
    StudentProfileResponseDTO getProfile(Long studentId);

    /**
     * Fetch student profile picture as InputStreamResource
     */
    InputStreamResource getProfilePicture(Long studentId);
}
