package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.TrainingSkillWorkshopDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.TrainingSkillWorkshop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainingSkillWorkshopMapper {

    // Entity → DTO
    TrainingSkillWorkshopDTO toDTO(TrainingSkillWorkshop trainingSkillWorkshop);

    // DTO → Entity
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    TrainingSkillWorkshop toEntity(TrainingSkillWorkshopDTO dto);
    
    // Update existing entity from DTO
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    @Mapping(target = "id", ignore = true) // Don't update ID
    void updateEntityFromDto(TrainingSkillWorkshopDTO dto, @MappingTarget TrainingSkillWorkshop entity);
}
