package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.ConsultancyProjectWorkDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.ConsultancyProjectWork;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultancyProjectWorkMapper {

    // Entity → DTO
    ConsultancyProjectWorkDTO toDTO(ConsultancyProjectWork consultancyProjectWork);

    // DTO → Entity
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    @Mapping(target = "applications", ignore = true) // Ignore bidirectional relationship
    ConsultancyProjectWork toEntity(ConsultancyProjectWorkDTO dto);
}
