package in.edu.jspmjscoe.admissionportal.mappers.achievements.fieldproject;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectViewDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FieldProjectViewMapper {


    @Mapping(target = "videoUrl", source = "videoMinioKey")
    @Mapping(target = "pdfUrl", source = "pdfMinioKey")
    @Mapping(target = "subjectId", source = "subject.subjectId")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "unitNumber", source = "unitNumber")
    public abstract FieldProjectViewDTO toDTO(FieldProject fieldProject);


}
