package in.edu.jspmjscoe.admissionportal.mappers.achievements.miniproject;


import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.MiniProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MiniProjectMapper {

    @Mapping(target = "studentAcademicYearId", source = "studentAcademicYear.studentAcademicYearId")
    MiniProjectDTO toDTO(MiniProject miniProject);

    @Mapping(target = "studentAcademicYear.studentAcademicYearId", source = "studentAcademicYearId")
    MiniProject toEntity(MiniProjectDTO dto);
}
