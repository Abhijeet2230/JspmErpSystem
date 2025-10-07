package in.edu.jspmjscoe.admissionportal.services.impl.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileUpdateDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.minio.MinioStorageException;
import in.edu.jspmjscoe.admissionportal.mappers.student.StudentProfileMapper;
import in.edu.jspmjscoe.admissionportal.model.student.Address;
import in.edu.jspmjscoe.admissionportal.model.student.Parent;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.impl.minio.MinioStorageService;
import in.edu.jspmjscoe.admissionportal.services.student.StudentProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentRepository studentRepository;
    private final StudentProfileMapper mapper;
    private final MinioStorageService minioStorageService;

    // -----------------------
    // Update profile
    // -----------------------
    @Override
    public StudentProfileResponseDTO updateProfile(Long studentId, StudentProfileUpdateDTO dto, MultipartFile profilePicture) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Ensure nested entities exist
        if (student.getParent() == null) student.setParent(new Parent());

        student.getParent().setStudent(student);

        if (student.getAddress() == null) student.setAddress(new Address());

        student.getAddress().setStudent(student);
        // Map DTO fields
        mapper.updateStudentFromDto(dto, student);

        // Handle profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String objectKey = minioStorageService.uploadStudentProfilePic(profilePicture, student.getStudentId());
                student.setProfilePicturePath(objectKey);
                log.info("Profile picture uploaded for studentId={}, objectKey={}", studentId, objectKey);
            } catch (Exception e) {
                throw new MinioStorageException("Failed to upload profile picture", e);
            }
        }

        studentRepository.save(student);

        // Return updated profile
        return mapper.toDto(student);
    }

    // -----------------------
    // Get profile
    // -----------------------
    @Override
    public StudentProfileResponseDTO getProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return mapper.toDto(student);
    }

    // -----------------------
    // Get profile picture
    // -----------------------
    @Override
    public InputStreamResource getProfilePicture(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (student.getProfilePicturePath() == null) {
            throw new ResourceNotFoundException("Profile picture not set for student");
        }

        return minioStorageService.getFile(student.getProfilePicturePath());
    }
}
