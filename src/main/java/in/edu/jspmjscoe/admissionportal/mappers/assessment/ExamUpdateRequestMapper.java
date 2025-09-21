package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.ExamUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExamUpdateRequestMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ExamUpdateRequestDTO dto, @MappingTarget StudentExam entity);
}
