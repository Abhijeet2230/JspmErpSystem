package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.CompanyDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    // Entity → DTO
    CompanyDTO toDTO(Company company);

    // DTO → Entity
    Company toEntity(CompanyDTO dto);
    
    // Update existing entity from DTO
    void updateEntityFromDto(CompanyDTO dto, @MappingTarget Company entity);
}
