package in.edu.jspmjscoe.admissionportal.mappers.achievements.fieldproject;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FieldProjectMapper {

    @Mapping(target = "studentAcademicYearId", source = "studentAcademicYear.studentAcademicYearId")
    FieldProjectDTO toDTO(FieldProject fieldProject);

    @Mapping(target = "studentAcademicYear.studentAcademicYearId", source = "studentAcademicYearId")
    FieldProject toEntity(FieldProjectDTO dto);
}
