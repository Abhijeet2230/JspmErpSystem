package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.UnitUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UnitUpdateRequestMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UnitUpdateRequestDTO dto, @MappingTarget StudentUnitAssessment entity);
}
