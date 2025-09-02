package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.StudentDTO;
import in.edu.jspmjscoe.admissionportal.model.*;
import in.edu.jspmjscoe.admissionportal.mappers.*;
import in.edu.jspmjscoe.admissionportal.repositories.*;
import in.edu.jspmjscoe.admissionportal.helper.ExcelHelper;
import in.edu.jspmjscoe.admissionportal.services.ExcelImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final StudentMapper studentMapper;
    private final ParentMapper parentMapper;
    private final AddressMapper addressMapper;
    private final SSCMapper sscMapper;
    private final HSCMapper hscMapper;
    private final CETMapper cetMapper;
    private final JEEMapper jeeMapper;
    private final AdmissionMapper admissionMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public int importStudents(MultipartFile file) {
        try {
            List<StudentDTO> studentDTOs = ExcelHelper.excelToStudentDTOs(file.getInputStream());

            Role studentRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("ROLE_STUDENT not found"));

            for (StudentDTO dto : studentDTOs) {

                // Create User entity
                User user = new User();
                user.setUserName(dto.getApplicationId());
                user.setPassword(passwordEncoder.encode(dto.getDob())); // default password
                user.setFirstLogin(true);
                user.setEnabled(true);
                user.setRole(studentRole);

                // Create Student entity and link to User
                Student student = studentMapper.toEntity(dto);
                student.setUser(user);
                user.setStudent(student);

                // Convert gender string to enum
                if (dto.getGender() != null && !dto.getGender().isBlank()) {
                    student.setGender(Gender.valueOf(dto.getGender().toUpperCase().replace(" ", "_")));
                }

                // Save student first to avoid transient issues
                studentRepository.save(student);

                // Map and save child entities
                if (dto.getParent() != null) {
                    Parent parent = parentMapper.toEntity(dto.getParent());
                    parent.setStudent(student);
                    student.setParent(parent);
                }

                if (dto.getAddress() != null) {
                    Address address = addressMapper.toEntity(dto.getAddress());
                    address.setStudent(student);
                    student.setAddress(address);
                }

                if (dto.getSsc() != null) {
                    SSC ssc = sscMapper.toEntity(dto.getSsc());
                    ssc.setStudent(student);
                    student.setSsc(ssc);
                }

                if (dto.getHsc() != null) {
                    HSC hsc = hscMapper.toEntity(dto.getHsc());
                    hsc.setStudent(student);
                    student.setHsc(hsc);
                }

                if (dto.getCet() != null) {
                    CET cet = cetMapper.toEntity(dto.getCet());
                    cet.setStudent(student);
                    student.setCet(cet);
                }

                if (dto.getJee() != null && dto.getJee().getApplicationNo() != null
                        && !dto.getJee().getApplicationNo().isBlank()) {
                    JEE jee = jeeMapper.toEntity(dto.getJee());
                    jee.setStudent(student);
                    student.setJee(jee);
                }

                if (dto.getAdmissions() != null && !dto.getAdmissions().isEmpty()) {
                    dto.getAdmissions().forEach(admissionDTO -> {
                        var admission = admissionMapper.toEntity(admissionDTO);
                        admission.setStudent(student);

                        // Set parsed dates from DTO
                        admission.setAdmissionDate(admissionDTO.getAdmissionDate());
                        admission.setReportedDate(admissionDTO.getReportedDate());

                        student.getAdmissions().add(admission);
                    });
                }

                // Final save to persist all children
                studentRepository.save(student);
            }

            return studentDTOs.size();
        } catch (Exception e) {
            throw new RuntimeException("Failed to import Excel file: " + e.getMessage(), e);
        }
    }
}
