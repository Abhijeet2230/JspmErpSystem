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
import in.edu.jspmjscoe.admissionportal.services.impl.minio.MinioStorageService;
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
}