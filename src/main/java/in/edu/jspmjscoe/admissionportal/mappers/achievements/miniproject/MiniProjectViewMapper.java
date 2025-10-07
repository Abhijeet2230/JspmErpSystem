package in.edu.jspmjscoe.admissionportal.mappers.achievements.miniproject;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectViewDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.MiniProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class MiniProjectViewMapper {

    @Mapping(target = "videoUrl", source = "videoMinioKey")
    @Mapping(target = "pdfUrl", source = "pdfMinioKey")
    public abstract MiniProjectViewDTO toDTO(MiniProject miniProject);

}
