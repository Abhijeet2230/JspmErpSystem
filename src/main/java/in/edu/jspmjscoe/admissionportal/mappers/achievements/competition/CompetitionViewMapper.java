package in.edu.jspmjscoe.admissionportal.mappers.achievements.competition;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionViewDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.Competition;
import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CompetitionViewMapper {

    @Autowired
    protected MinioStorageService minioStorageService;

    @Mapping(target = "fileUrl", source = "minioObjectKey", qualifiedByName = "objectKeyToUrl")
    public abstract CompetitionViewDTO toDTO(Competition competition);

    @Named("objectKeyToUrl")
    protected String objectKeyToUrl(String objectKey) {
        if (objectKey == null) return null;
        return minioStorageService.getPresignedUrl(objectKey);
    }
}
