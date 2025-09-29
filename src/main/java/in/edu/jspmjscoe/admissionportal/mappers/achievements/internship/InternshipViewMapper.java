package in.edu.jspmjscoe.admissionportal.mappers.achievements.internship;


import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipViewDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.Internship;
import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class InternshipViewMapper {

    @Autowired
    protected MinioStorageService minioStorageService;

    @Mapping(target = "fileUrl", source = "minioObjectKey", qualifiedByName = "objectKeyToUrl")
    public abstract InternshipViewDTO toDTO(Internship internship);

    @Named("objectKeyToUrl")
    protected String objectKeyToUrl(String objectKey) {
        if (objectKey == null) return null;
        return minioStorageService.getPresignedUrl(objectKey);
    }
}
