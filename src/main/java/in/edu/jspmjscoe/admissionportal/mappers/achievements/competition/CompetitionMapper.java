package in.edu.jspmjscoe.admissionportal.mappers.achievements.competition;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.Competition;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CompetitionMapper {

    @Mapping(target = "studentAcademicYearId", source = "studentAcademicYear.studentAcademicYearId")
    CompetitionDTO toDTO(Competition competition);

    @Mapping(target = "studentAcademicYear.studentAcademicYearId", source = "studentAcademicYearId")
    Competition toEntity(CompetitionDTO dto);
}
