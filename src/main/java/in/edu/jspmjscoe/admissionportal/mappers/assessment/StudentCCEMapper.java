package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentCCEDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentCCEMapper {

    // ---------- Entity -> DTO ----------
    StudentCCEDTO toDto(Student entity);

    // ---------- DTO -> Entity (optional) ----------
    Student toEntity(StudentCCEDTO dto);
}
