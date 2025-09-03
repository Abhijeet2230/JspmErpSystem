package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.StudentDTO;
import in.edu.jspmjscoe.admissionportal.helper.ExcelHelper;
import in.edu.jspmjscoe.admissionportal.mappers.*;
import in.edu.jspmjscoe.admissionportal.model.*;
import in.edu.jspmjscoe.admissionportal.repositories.*;
import in.edu.jspmjscoe.admissionportal.services.ExcelImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.util.List;

@Slf4j
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
            System.out.println(studentDTOs);
            // 🔍 Print all parsed StudentDTOs at start
            log.info(">>> Total students parsed from Excel = {}", studentDTOs.size());
            for (StudentDTO dto : studentDTOs) {
                log.debug("Parsed DTO: applicationId={}, candidate={}, dob={}, gender={}, admissionsCount={}",
                        dto.getApplicationId(),
                        dto.getCandidateName(),
                        dto.getDob(),
                        dto.getGender(),
                        (dto.getAdmissions() != null ? dto.getAdmissions().size() : 0)
                );
            }

            Role studentRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("ROLE_STUDENT not found"));

            int importedCount = 0;

            for (StudentDTO dto : studentDTOs) {

                // 🔍 Skip if student with applicationId already exists
                if (studentRepository.findByApplicationId(dto.getApplicationId()).isPresent()) {
                    log.warn("Skipping duplicate: applicationId={}", dto.getApplicationId());
                    continue;
                }

                // ✅ Create User
                User user = new User();
                user.setUserName(dto.getApplicationId());
                user.setPassword(passwordEncoder.encode(dto.getDob())); // default password = dob
                user.setFirstLogin(true);
                user.setEnabled(true);
                user.setRole(studentRole);

                // ✅ Create Student
                Student student = studentMapper.toEntity(dto);

                // set bidirectional link
                student.setUser(user);
                user.setStudent(student);

                // ✅ Handle gender safely
                if (dto.getGender() != null && !dto.getGender().isBlank()) {
                    try {
                        student.setGender(Gender.valueOf(dto.getGender().toUpperCase().replace(" ", "_")));
                    } catch (IllegalArgumentException ignored) {
                        log.warn("Invalid gender value for applicationId={}: {}", dto.getApplicationId(), dto.getGender());
                    }
                }

                // ✅ Parent
                if (dto.getParent() != null) {
                    Parent parent = parentMapper.toEntity(dto.getParent());
                    parent.setStudent(student);
                    student.setParent(parent);
                }

                // ✅ Address
                if (dto.getAddress() != null) {
                    Address address = addressMapper.toEntity(dto.getAddress());
                    address.setStudent(student);
                    student.setAddress(address);
                }

                // ✅ SSC
                if (dto.getSsc() != null) {
                    SSC ssc = sscMapper.toEntity(dto.getSsc());
                    ssc.setStudent(student);
                    student.setSsc(ssc);
                }

                // ✅ HSC
                if (dto.getHsc() != null) {
                    HSC hsc = hscMapper.toEntity(dto.getHsc());
                    hsc.setStudent(student);
                    student.setHsc(hsc);
                }

                // ✅ CET
                if (dto.getCet() != null) {
                    CET cet = cetMapper.toEntity(dto.getCet());
                    cet.setStudent(student);
                    student.setCet(cet);
                }

                if (dto.getJee() != null
                        && dto.getJee().getApplicationNo() != null
                        && !dto.getJee().getApplicationNo().trim().isEmpty()) {

                    JEE jee = jeeMapper.toEntity(dto.getJee());
                    jee.setStudent(student);
                    student.setJee(jee);
                }


                // ✅ Admissions
                if (dto.getAdmissions() != null && !dto.getAdmissions().isEmpty()) {
                    dto.getAdmissions().forEach(admissionDTO -> {
                        Admission admission = admissionMapper.toEntity(admissionDTO);
                        admission.setStudent(student);

                        // Dates already parsed inside ExcelHelper
                        admission.setAdmissionDate(admissionDTO.getAdmissionDate());
                        admission.setReportedDate(admissionDTO.getReportedDate());

                        student.getAdmissions().add(admission);
                    });
                }

                // 🔍 Print entity status before save
                log.info("Preparing to save Student: applicationId={}, hasJee={}, admissionsCount={}, hasUser={}, hasParent={}, hasAddress={}",
                        student.getApplicationId(),
                        (student.getJee() != null ? "YES (student=" + (student.getJee().getStudent() != null) + ")" : "NO"),
                        (student.getAdmissions() != null ? student.getAdmissions().size() : 0),
                        (student.getUser() != null),
                        (student.getParent() != null),
                        (student.getAddress() != null)
                );

                // 🚀 Save once (cascade takes care of user + children)
                studentRepository.save(student);
                importedCount++;
            }

            log.info(">>> Import completed successfully: {} students saved", importedCount);
            return importedCount;

        } catch (Exception e) {
            log.error(">>> Failed to import Excel file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to import Excel file: " + e.getMessage(), e);
        }
    }

}
