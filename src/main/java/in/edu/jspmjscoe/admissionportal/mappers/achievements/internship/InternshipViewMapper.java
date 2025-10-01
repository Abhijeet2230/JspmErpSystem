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


    @Mapping(target = "fileUrl", source = "minioObjectKey")
    public abstract InternshipViewDTO toDTO(Internship internship);


}
