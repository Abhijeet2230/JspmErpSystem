package in.edu.jspmjscoe.admissionportal.services.impl.excel;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.helper.TeacherExcelHelper;
import in.edu.jspmjscoe.admissionportal.model.security.AppRole;
import in.edu.jspmjscoe.admissionportal.model.security.Role;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.repositories.security.RoleRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.DepartmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.services.excel.TeacherExcelImportService;
import in.edu.jspmjscoe.admissionportal.exception.ExcelImportException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherExcelImportServiceImpl implements TeacherExcelImportService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public int importTeachers(MultipartFile file, int headerRowNumber) {
        try {
            if (file == null || file.isEmpty()) {
                throw new ExcelImportException("Uploaded file is empty");
            }

            List<TeacherDTO> teacherDTOs = TeacherExcelHelper.excelToTeacherDTOs(file.getInputStream());
            log.info("Parsed {} teacher rows from Excel", teacherDTOs.size());

            Role teacherRole = roleRepository.findByRoleName(AppRole.ROLE_TEACHER)
                    .orElseThrow(() -> new ExcelImportException("ROLE_TEACHER not found in DB"));

            int imported = 0;

            for (TeacherDTO dto : teacherDTOs) {

                // duplicate checks: employeeId, officialEmail, aadhaarNumber
                boolean duplicate = false;
                if (dto.getEmployeeId() != null && !dto.getEmployeeId().isBlank()) {
                    if (teacherRepository.findByEmployeeId(dto.getEmployeeId()).isPresent()) {
                        log.warn("Skipping duplicate teacher (employeeId): {}", dto.getEmployeeId());
                        duplicate = true;
                    }
                }
                if (!duplicate && dto.getOfficialEmail() != null && !dto.getOfficialEmail().isBlank()) {
                    if (teacherRepository.findByOfficialEmail(dto.getOfficialEmail()).isPresent()) {
                        log.warn("Skipping duplicate teacher (officialEmail): {}", dto.getOfficialEmail());
                        duplicate = true;
                    }
                }
                if (!duplicate && dto.getAadhaarNumber() != null && !dto.getAadhaarNumber().isBlank()) {
                    if (teacherRepository.findByAadhaarNumber(dto.getAadhaarNumber()).isPresent()) {
                        log.warn("Skipping duplicate teacher (aadhaar): {}", dto.getAadhaarNumber());
                        duplicate = true;
                    }
                }
                if (duplicate) continue;

                // Resolve department
                Department department = resolveDepartment(dto.getDepartmentName());
                if (department == null) {
                    log.warn("Skipping teacher because department not found: {} (teacher: {} {})",
                            dto.getDepartmentName(), dto.getFirstName(), dto.getLastName());
                    continue; // skip or throw depending on preference
                }

                // Create User
                User user = new User();
                String username = dto.getEmployeeId() != null && !dto.getEmployeeId().isBlank()
                        ? dto.getEmployeeId()
                        : (dto.getOfficialEmail() != null ? dto.getOfficialEmail() : dto.getPersonalEmail());
                if (username == null) username = "tuser_" + System.currentTimeMillis();

                user.setUserName(username);
                String rawPassword = dto.getDateOfBirth() != null ? dto.getDateOfBirth() : "changeme";
                user.setPassword(passwordEncoder.encode(rawPassword));
                user.setRole(teacherRole);
                user.setEnabled(true);
                user.setFirstLogin(true);

                // Map Teacher entity
                Teacher teacher = new Teacher();
                teacher.setFirstName(dto.getFirstName());
                teacher.setMiddleName(dto.getMiddleName());
                teacher.setLastName(dto.getLastName());
                teacher.setPrefix(dto.getPrefix());
                teacher.setGender(dto.getGender());
                teacher.setDateOfBirth(dto.getDateOfBirth());
                teacher.setPhone(dto.getPhone());
                teacher.setPersonalEmail(dto.getPersonalEmail());

                teacher.setOfficialEmail(dto.getOfficialEmail());
                teacher.setDesignation(dto.getDesignation());
                teacher.setEmployeeId(dto.getEmployeeId());
                teacher.setBcudId(dto.getBcudId());
                teacher.setVidwaanId(dto.getVidwaanId());
                teacher.setOrchidId(dto.getOrchidId());
                teacher.setGoogleScholarId(dto.getGoogleScholarId());

                teacher.setHighestDegree(dto.getHighestDegree());
                teacher.setPhdYear(dto.getPhdYear());
                teacher.setSpecialization(dto.getSpecialization());
                teacher.setDegreeUniversity(dto.getDegreeUniversity());

                teacher.setPreviousInstitutions(dto.getPreviousInstitutions());
                teacher.setYearsExperience(dto.getYearsExperience());
                teacher.setSubjectsTaught(dto.getSubjectsTaught());

                teacher.setDepartment(department);

                // link user <-> teacher (mirrors student import approach)
                teacher.setUser(user);
                try {
                    // If User entity has setTeacher, set it. If not, this will be ignored at compile-time,
                    // but in your project User probably has setTeacher() like it had setStudent().
                    user.getClass().getMethod("setTeacher", Teacher.class).invoke(user, teacher);
                } catch (NoSuchMethodException ignored) {
                    // If User doesn't have setTeacher, that's fine — the relationship still exists on Teacher side.
                    // Most likely your User model already has setTeacher/setStudent methods.
                } catch (Exception e) {
                    log.debug("Could not call user.setTeacher via reflection: {}", e.getMessage());
                }

                teacherRepository.saveAndFlush(teacher);
                imported++;
            }

            log.info("Teacher import completed. Imported = {}", imported);
            return imported;

        } catch (ExcelImportException e) {
            log.error("Teacher import error: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to import teachers: {}", e.getMessage(), e);
            throw new ExcelImportException("Failed to import teachers: " + e.getMessage(), e);
        }
    }

    private Department resolveDepartment(String deptText) {


        if (deptText == null || deptText.isBlank()) return null;
        String cleaned = deptText.trim();

        // Debug log
        log.debug("Looking up Department for '{}'", cleaned);
        // Try repository methods (case-insensitive)
        Optional<Department> byName = departmentRepository.findByNameIgnoreCase(cleaned);
        if (byName.isPresent()) return byName.get();

        Optional<Department> byCode = departmentRepository.findByCodeIgnoreCase(cleaned);
        if (byCode.isPresent()) return byCode.get();

        // Department not found → create new
        Department newDept = new Department();
        newDept.setName(cleaned);

        // Optional: generate a code automatically if your entity requires it
        newDept.setCode(cleaned.replaceAll("\\s+", "_").toUpperCase());

        departmentRepository.saveAndFlush(newDept);
        log.info("Created new Department '{}'", cleaned);

        return newDept;
    }

}
