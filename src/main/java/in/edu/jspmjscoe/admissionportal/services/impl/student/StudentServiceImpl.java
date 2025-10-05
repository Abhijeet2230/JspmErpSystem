package in.edu.jspmjscoe.admissionportal.services.impl.student;

import in.edu.jspmjscoe.admissionportal.dtos.security.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileUpdateDTO;
import in.edu.jspmjscoe.admissionportal.exception.security.InvalidCredentialsException;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.student.StudentMapper;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Address;
import in.edu.jspmjscoe.admissionportal.model.student.BloodGroup;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
import in.edu.jspmjscoe.admissionportal.services.student.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MinioStorageService minioStorageService;


    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }


    public void changePassword(String username, ChangePasswordRequest request) {
        // Load user
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        // Set new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Flip first login flag
        if (user.isFirstLogin()) {
            user.setFirstLogin(false);
        }

        userRepository.save(user);
    }


    @Transactional
    public Student updateProfile(User user, StudentProfileUpdateDTO dto) {
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // 1. Update blood group
        if (dto.getBloodGroup() != null && !dto.getBloodGroup().isEmpty()) {
            try {
                BloodGroup bg = BloodGroup.valueOf(dto.getBloodGroup().toUpperCase().replace("+", "_POSITIVE").replace("-", "_NEGATIVE"));
                student.setBloodGroup(bg);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid blood group: " + dto.getBloodGroup());
            }
        }


        // 2. Update parent contact numbers
        if (student.getParent() != null) {
            if (dto.getFatherMobileNo() != null) {
                student.getParent().setFatherMobileNo(dto.getFatherMobileNo());
            }
            if (dto.getMotherMobileNo() != null) {
                student.getParent().setMotherMobileNo(dto.getMotherMobileNo());
            }
        }

        // 3. Update address
        if (student.getAddress() != null) {
            Address address = student.getAddress();
            address.setAddressLine1(dto.getAddressLine1());
            address.setAddressLine2(dto.getAddressLine2());
            address.setAddressLine3(dto.getAddressLine3());
            address.setState(dto.getState());
            address.setDistrict(dto.getDistrict());
            address.setTaluka(dto.getTaluka());
            address.setVillage(dto.getVillage());
            address.setPincode(dto.getPincode());
        }

        // 4. Upload profile picture
        if (dto.getProfilePicture() != null && !dto.getProfilePicture().isEmpty()) {
            String path = minioStorageService.uploadStudentProfilePic(dto.getProfilePicture(), student.getStudentId());
            student.setProfilePicturePath(path);
        }


        return studentRepository.save(student);
    }

}
