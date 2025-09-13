package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.*;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final AdmissionRepository admissionRepository; // ✅ Added to save admissions separately
    private final StudentMapper studentMapper;
    private final ParentMapper parentMapper;
    private final AddressMapper addressMapper;
    private final SSCMapper sscMapper;
    private final HSCMapper hscMapper;
    private final EntranceExamMapper entranceExamMapper;
    private final AdmissionMapper admissionMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public int importStudents(MultipartFile file) {
        try {
            List<StudentDTO> studentDTOs = ExcelHelper.excelToStudentDTOs(file.getInputStream());
            log.info(">>> Total students parsed from Excel = {}", studentDTOs.size());

            Role studentRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("ROLE_STUDENT not found"));

            int importedCount = 0;

            for (StudentDTO dto : studentDTOs) {

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
                student.setUser(user);
                user.setStudent(student);

                // ✅ Gender
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

                // ✅ Entrance Exams (CET + JEE now combined)
                List<EntranceExam> exams = new ArrayList<>();
                if (dto.getEntranceExams() != null && !dto.getEntranceExams().isEmpty()) {
                    for (EntranceExamDTO examDTO : dto.getEntranceExams()) {
                        EntranceExam exam = entranceExamMapper.toEntity(examDTO);
                        exam.setStudent(student); // attach student reference
                        exams.add(exam);
                    }
                }
                student.setEntranceExams(exams);

                // ✅ STEP 1: Detach Admissions before saving Student
                List<AdmissionDTO> admissionDTOs = new ArrayList<>();
                if (dto.getAdmissions() != null) {
                    admissionDTOs.addAll(dto.getAdmissions());
                }
                student.setAdmissions(new ArrayList<>()); // prevent cascade

                // ✅ STEP 2: Save Student first (no admissions)
                Student savedStudent = studentRepository.saveAndFlush(student);

                // ✅ STEP 3: Now save Admissions separately
                for (AdmissionDTO admissionDTO : admissionDTOs) {
                    Admission admission = admissionMapper.toEntity(admissionDTO);
                    admission.setStudent(savedStudent);
                    admission.setAdmissionDate(admissionDTO.getAdmissionDate());
                    admission.setReportedDate(admissionDTO.getReportedDate());
                    admission.setIsCurrent(true);
                    admission.setAdmissionStatus(AdmissionStatus.PENDING);
                    admission.setCurrentYear(CurrentYear.FINAL_YEAR);
                    admissionRepository.save(admission);
                }

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
