package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipPostingDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.Company;
import in.edu.jspmjscoe.admissionportal.model.internship.InternshipPosting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InternshipPostingMapper {

    // Entity → DTO
    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    InternshipPostingDTO toDTO(InternshipPosting internshipPosting);

    // DTO → Entity
    @Mapping(target = "company", expression = "java(mapCompany(dto.getCompanyId()))")
    @Mapping(target = "applications", ignore = true) // Ignore bidirectional relationship
    InternshipPosting toEntity(InternshipPostingDTO dto);
    
    // Update existing entity from DTO
    @Mapping(target = "company", ignore = true) // Don't update company relationship
    @Mapping(target = "applications", ignore = true) // Ignore bidirectional relationship
    @Mapping(target = "internshipId", ignore = true) // Don't update ID
    void updateEntityFromDto(InternshipPostingDTO dto, @MappingTarget InternshipPosting entity);

    // Helper method to create Company reference with just ID
    default Company mapCompany(Long companyId) {
        if (companyId == null) return null;
        Company company = new Company();
        company.setCompanyId(companyId);
        return company;
    }
}
