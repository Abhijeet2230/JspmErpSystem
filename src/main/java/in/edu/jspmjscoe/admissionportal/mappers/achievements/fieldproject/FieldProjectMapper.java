package in.edu.jspmjscoe.admissionportal.mappers.achievements.fieldproject;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FieldProjectMapper {


    // Entity → DTO
    @Mapping(target = "studentAcademicYearId", source = "studentAcademicYear.studentAcademicYearId")
    @Mapping(target = "subjectId", source = "subject.subjectId")
    FieldProjectDTO toDTO(FieldProject fieldProject);

    // DTO → Entity
    @Mapping(target = "studentAcademicYear.studentAcademicYearId", source = "studentAcademicYearId")
    @Mapping(target = "subject.subjectId", source = "subjectId")
    FieldProject toEntity(FieldProjectDTO dto);

}
