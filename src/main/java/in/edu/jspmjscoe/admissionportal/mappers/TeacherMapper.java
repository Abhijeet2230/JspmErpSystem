package in.edu.jspmjscoe.admissionportal.mappers;

import in.edu.jspmjscoe.admissionportal.dtos.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.dtos.TeacherSubjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.TeacherAddressDTO;
import in.edu.jspmjscoe.admissionportal.model.*;

import java.util.stream.Collectors;

public class TeacherMapper {

    // -------------------- TO DTO --------------------
    public static TeacherDTO toDTO(Teacher teacher) {
        if (teacher == null) return null;

        return TeacherDTO.builder()
                .teacherId(teacher.getTeacherId())
                .userId(teacher.getUser() != null ? teacher.getUser().getUserId() : null)
                .departmentId(teacher.getDepartment() != null ? teacher.getDepartment().getDepartmentId() : null)
                .departmentName(teacher.getDepartment() != null ? teacher.getDepartment().getName() : null)

                // Personal
                .prefix(teacher.getPrefix())
                .firstName(teacher.getFirstName())
                .middleName(teacher.getMiddleName())
                .lastName(teacher.getLastName())
                .gender(teacher.getGender())
                .dateOfBirth(teacher.getDateOfBirth())
                .phone(teacher.getPhone())
                .personalEmail(teacher.getPersonalEmail())
                .aadhaarNumber(teacher.getAadhaarNumber())

                // Professional
                .officialEmail(teacher.getOfficialEmail())
                .designation(teacher.getDesignation())
                .employeeId(teacher.getEmployeeId())
                .bcudId(teacher.getBcudId())
                .vidwaanId(teacher.getVidwaanId())
                .orchidId(teacher.getOrchidId())
                .googleScholarId(teacher.getGoogleScholarId())

                // Academic
                .highestDegree(teacher.getHighestDegree())
                .phdYear(teacher.getPhdYear())
                .specialization(teacher.getSpecialization())
                .degreeUniversity(teacher.getDegreeUniversity())

                // Address
                .address(toAddressDTO(teacher.getAddress()))

                // Experience
                .previousInstitutions(teacher.getPreviousInstitutions())
                .yearsExperience(teacher.getYearsExperience())
                .subjectsTaught(teacher.getSubjectsTaught())

                // System
                .status(teacher.getStatus())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())

                // TeacherSubjects
                .teacherSubjects(
                        teacher.getTeacherSubjects() != null ?
                                teacher.getTeacherSubjects()
                                        .stream()
                                        .map(TeacherMapper::toTeacherSubjectDTO)
                                        .collect(Collectors.toList())
                                : null
                )
                .build();
    }

    // -------------------- TO ENTITY --------------------
    public static Teacher toEntity(TeacherDTO dto, User user, Department department) {
        if (dto == null) return null;

        Teacher teacher = new Teacher();
        teacher.setTeacherId(dto.getTeacherId());
        teacher.setUser(user);
        teacher.setDepartment(department);

        // Personal
        teacher.setPrefix(dto.getPrefix());
        teacher.setFirstName(dto.getFirstName());
        teacher.setMiddleName(dto.getMiddleName());
        teacher.setLastName(dto.getLastName());
        teacher.setGender(dto.getGender());
        teacher.setDateOfBirth(dto.getDateOfBirth());
        teacher.setPhone(dto.getPhone());
        teacher.setPersonalEmail(dto.getPersonalEmail());
        teacher.setAadhaarNumber(dto.getAadhaarNumber());

        // Professional
        teacher.setOfficialEmail(dto.getOfficialEmail());
        teacher.setDesignation(dto.getDesignation());
        teacher.setEmployeeId(dto.getEmployeeId());
        teacher.setBcudId(dto.getBcudId());
        teacher.setVidwaanId(dto.getVidwaanId());
        teacher.setOrchidId(dto.getOrchidId());
        teacher.setGoogleScholarId(dto.getGoogleScholarId());

        // Academic
        teacher.setHighestDegree(dto.getHighestDegree());
        teacher.setPhdYear(dto.getPhdYear());
        teacher.setSpecialization(dto.getSpecialization());
        teacher.setDegreeUniversity(dto.getDegreeUniversity());

        // Address
        if (dto.getAddress() != null) {
            teacher.setAddress(toAddressEntity(dto.getAddress(), teacher));
        }

        // Experience
        teacher.setPreviousInstitutions(dto.getPreviousInstitutions());
        teacher.setYearsExperience(dto.getYearsExperience());
        teacher.setSubjectsTaught(dto.getSubjectsTaught());

        // System
        teacher.setStatus(dto.getStatus());
        teacher.setCreatedAt(dto.getCreatedAt());
        teacher.setUpdatedAt(dto.getUpdatedAt());

        return teacher;
    }

    // -------------------- HELPER METHODS --------------------
    private static TeacherAddressDTO toAddressDTO(TeacherAddress address) {
        if (address == null) return null;

        return TeacherAddressDTO.builder()
                .addressId(address.getAddressId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .addressLine3(address.getAddressLine3())
                .state(address.getState())
                .district(address.getDistrict())
                .taluka(address.getTaluka())
                .village(address.getVillage())
                .pincode(address.getPincode())
                .build();
    }

    private static TeacherAddress toAddressEntity(TeacherAddressDTO dto, Teacher teacher) {
        if (dto == null) return null;

        return TeacherAddress.builder()
                .addressId(dto.getAddressId())
                .teacher(teacher)
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .addressLine3(dto.getAddressLine3())
                .state(dto.getState())
                .district(dto.getDistrict())
                .taluka(dto.getTaluka())
                .village(dto.getVillage())
                .pincode(dto.getPincode())
                .build();
    }

    private static TeacherSubjectDTO toTeacherSubjectDTO(TeacherSubject ts) {
        if (ts == null) return null;

        String teacherName = ts.getTeacher() != null ? buildFullName(ts.getTeacher()) : null;

        return TeacherSubjectDTO.builder()
                .teacherSubjectId(ts.getTeacherSubjectId())
                .teacherId(ts.getTeacher() != null ? ts.getTeacher().getTeacherId() : null)
                .teacherName(teacherName)
                .subjectId(ts.getSubject() != null ? ts.getSubject().getSubjectId() : null)
                .subjectName(ts.getSubject() != null ? ts.getSubject().getName() : null)
                .roleInSubject(ts.getRoleInSubject())
                .build();
    }

    private static String buildFullName(Teacher teacher) {
        if (teacher == null) return null;

        return (teacher.getFirstName() != null ? teacher.getFirstName() : "")
                + (teacher.getMiddleName() != null && !teacher.getMiddleName().isBlank()
                ? " " + teacher.getMiddleName()
                : "")
                + (teacher.getLastName() != null ? " " + teacher.getLastName() : "")
                .trim();
    }
}
