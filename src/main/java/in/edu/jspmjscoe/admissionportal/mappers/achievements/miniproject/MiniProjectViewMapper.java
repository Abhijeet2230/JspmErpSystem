package in.edu.jspmjscoe.admissionportal.mappers.achievements.miniproject;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectViewDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.MiniProject;
import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MiniProjectViewMapper {

    @Autowired
    protected MinioStorageService minioStorageService;

    @Mapping(target = "videoUrl", source = "videoMinioKey", qualifiedByName = "objectKeyToUrl")
    @Mapping(target = "pdfUrl", source = "pdfMinioKey", qualifiedByName = "objectKeyToUrl")
    public abstract MiniProjectViewDTO toDTO(MiniProject miniProject);

    @Named("objectKeyToUrl")
    protected String objectKeyToUrl(String objectKey) {
        if (objectKey == null) return null;
        return minioStorageService.getPresignedUrl(objectKey);
    }
}
