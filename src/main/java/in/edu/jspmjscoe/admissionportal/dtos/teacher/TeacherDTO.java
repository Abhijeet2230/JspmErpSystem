package in.edu.jspmjscoe.admissionportal.dtos.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.subject.TeacherSubjectDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Teacher entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDTO {

    private Long teacherId;

    private Long userId;
    private Long departmentId;
    private String departmentName;

    // 1. Personal
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String phone;
    private String personalEmail;
    private String aadhaarNumber;

    // 2. Professional
    private String officialEmail;
    private String designation;
    private String employeeId;
    private String bcudId;
    private String vidwaanId;
    private String orchidId;
    private String googleScholarId;

    // 3. Academic
    private String highestDegree;
    private Integer phdYear;
    private String specialization;
    private String degreeUniversity;

    // 4. Address
    private TeacherAddressDTO address;

    // 5. Experience
    private String previousInstitutions;
    private Integer yearsExperience;
    private String subjectsTaught;

    // System fields
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<TeacherSubjectDTO> teacherSubjects;
}
