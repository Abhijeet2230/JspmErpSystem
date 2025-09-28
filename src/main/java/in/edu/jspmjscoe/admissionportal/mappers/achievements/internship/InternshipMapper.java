package in.edu.jspmjscoe.admissionportal.mappers.achievements.internship;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.Internship;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InternshipMapper {

    @Mapping(target = "studentAcademicYearId", source = "studentAcademicYear.studentAcademicYearId")
    InternshipDTO toDTO(Internship internship);

    @Mapping(target = "studentAcademicYear.studentAcademicYearId", source = "studentAcademicYearId")
    Internship toEntity(InternshipDTO dto);
}
