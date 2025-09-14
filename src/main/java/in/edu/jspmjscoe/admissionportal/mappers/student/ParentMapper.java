package in.edu.jspmjscoe.admissionportal.mappers.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.ParentDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParentMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    ParentDTO toDTO(Parent parent);

    // DTO → Entity
    @Mapping(source = "studentId", target = "student.studentId")
    Parent toEntity(ParentDTO dto);

    void updateParentFromDTO(ParentDTO dto, @MappingTarget Parent entity);
}
