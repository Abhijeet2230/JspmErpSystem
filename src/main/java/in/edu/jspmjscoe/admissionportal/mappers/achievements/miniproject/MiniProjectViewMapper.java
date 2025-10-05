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

    @Mapping(target = "videoUrl", source = "videoMinioKey")
    @Mapping(target = "pdfUrl", source = "pdfMinioKey")
    public abstract MiniProjectViewDTO toDTO(MiniProject miniProject);

}
