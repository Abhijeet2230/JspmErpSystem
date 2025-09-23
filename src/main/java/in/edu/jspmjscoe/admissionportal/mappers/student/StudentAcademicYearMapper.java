package in.edu.jspmjscoe.admissionportal.mappers.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentAcademicYearDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentAcademicYearMapper {

    // Entity -> DTO
    @Mapping(source = "student.studentId", target = "studentId")
    StudentAcademicYearDTO toDTO(StudentAcademicYear entity);

    // DTO -> Entity (pass Student explicitly)
    @Mapping(target = "studentAcademicYearId", ignore = true) // DB generates ID
    StudentAcademicYear toEntity(StudentAcademicYearDTO dto, @Context Student student);

    // Update existing entity from DTO (pass Student if needed)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(StudentAcademicYearDTO dto, @MappingTarget StudentAcademicYear entity, @Context Student student);
}
