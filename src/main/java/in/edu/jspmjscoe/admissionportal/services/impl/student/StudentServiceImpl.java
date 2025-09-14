package in.edu.jspmjscoe.admissionportal.services.impl.student;

import in.edu.jspmjscoe.admissionportal.dtos.security.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.mappers.student.StudentMapper;
import in.edu.jspmjscoe.admissionportal.model.student.Gender;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);
        return studentMapper.toDTO(studentRepository.save(student));
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(studentMapper::toDTO).orElse(null);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        return studentRepository.findById(id).map(student -> {
            student.setApplicationId(studentDTO.getApplicationId());
            student.setCandidateName(studentDTO.getCandidateName());
            student.setMobileNo(studentDTO.getMobileNo());
            student.setEmail(studentDTO.getEmail());
            student.setGender(Gender.valueOf(studentDTO.getGender().toUpperCase()));
            student.setDob(studentDTO.getDob());
            student.setReligion(studentDTO.getReligion());
            student.setRegion(studentDTO.getRegion());
            student.setMotherTongue(studentDTO.getMotherTongue());
            student.setAnnualFamilyIncome(studentDTO.getAnnualFamilyIncome());
            student.setCandidatureType(studentDTO.getCandidatureType());
            student.setHomeUniversity(studentDTO.getHomeUniversity());
            student.setCategory(studentDTO.getCategory());
            student.setPhType(studentDTO.getPhType());
            student.setDefenceType(studentDTO.getDefenceType());
            student.setLinguisticMinority(studentDTO.getLinguisticMinority());
            student.setReligiousMinority(studentDTO.getReligiousMinority());
            // relations like parent, address, ssc, hsc, cet, jee, admissions 
            // can also be updated via their own service or cascaded
            return studentMapper.toDTO(studentRepository.save(student));
        }).orElse(null);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }



    public void changePassword(String username, ChangePasswordRequest request) {
        // Load user
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Set new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));


        // Flip first login flag
        if (user.isFirstLogin()) {       // only if true
            user.setFirstLogin(false);   // mark as changed
        }

        userRepository.save(user);
    }
}
