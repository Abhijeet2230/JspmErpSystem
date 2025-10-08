package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.Company;
import in.edu.jspmjscoe.admissionportal.model.internship.Placement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlacementMapper {

    // Entity → DTO
    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    PlacementDTO toDTO(Placement placement);

    // DTO → Entity
    @Mapping(target = "company", expression = "java(mapCompany(dto.getCompanyId()))")
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    @Mapping(target = "applications", ignore = true) // Ignore bidirectional relationship
    Placement toEntity(PlacementDTO dto);
    
    // Update existing entity from DTO
    @Mapping(target = "company", ignore = true) // Don't update company relationship
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    @Mapping(target = "applications", ignore = true) // Ignore bidirectional relationship
    @Mapping(target = "placementId", ignore = true) // Don't update ID
    void updateEntityFromDto(PlacementDTO dto, @MappingTarget Placement entity);

    // Helper method to create Company reference with just ID
    default Company mapCompany(Long companyId) {
        if (companyId == null) return null;
        Company company = new Company();
        company.setCompanyId(companyId);
        return company;
    }
}
