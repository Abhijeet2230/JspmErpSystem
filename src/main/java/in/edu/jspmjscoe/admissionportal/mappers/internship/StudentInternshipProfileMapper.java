package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.StudentProfileDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.StudentProfile;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentInternshipProfileMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    StudentProfileDTO toDTO(StudentProfile studentProfile);

    // DTO → Entity
    @Mapping(target = "student", expression = "java(mapStudent(dto.getStudentId()))")
    StudentProfile toEntity(StudentProfileDTO dto);

    // Helper method to create Student reference with just ID
    default Student mapStudent(Long studentId) {
        if (studentId == null) return null;
        Student student = new Student();
        student.setStudentId(studentId);
        return student;
    }
}
