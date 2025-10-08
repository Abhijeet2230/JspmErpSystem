package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.IndustrialVisitDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.IndustrialVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IndustrialVisitMapper {

    // Entity → DTO
    IndustrialVisitDTO toDTO(IndustrialVisit industrialVisit);

    // DTO → Entity
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    IndustrialVisit toEntity(IndustrialVisitDTO dto);
    
    // Update existing entity from DTO
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    @Mapping(target = "id", ignore = true) // Don't update ID
    void updateEntityFromDto(IndustrialVisitDTO dto, @MappingTarget IndustrialVisit entity);
}
