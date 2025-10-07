package in.edu.jspmjscoe.admissionportal.mappers.achievements.competition;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionViewDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.Competition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CompetitionViewMapper {

    @Mapping(target = "fileUrl", source = "minioObjectKey")
    public abstract CompetitionViewDTO toDTO(Competition competition);


}
