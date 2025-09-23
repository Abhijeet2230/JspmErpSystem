package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentCCEDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentCCEMapper {

    // ---------- Entity -> DTO ----------
    @Mapping(source = "studentAcademicYearId", target = "studentAcademicYearId")
    @Mapping(source = "student.candidateName", target = "candidateName")
    @Mapping(source = "rollNo", target = "rollNo")
    @Mapping(source = "division", target = "division")
    StudentCCEDTO toDto(StudentAcademicYear entity);

    // ---------- DTO -> Entity ----------
    // For creation, you usually set StudentAcademicYear in service, so ignore here
    @Mapping(target = "student", ignore = true)
    StudentAcademicYear toEntity(StudentCCEDTO dto);
}
