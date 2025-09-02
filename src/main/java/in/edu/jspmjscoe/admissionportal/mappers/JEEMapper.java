package in.edu.jspmjscoe.admissionportal.mappers;

import in.edu.jspmjscoe.admissionportal.dtos.JEEDTO;
import in.edu.jspmjscoe.admissionportal.model.JEE;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JEEMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    JEEDTO toDTO(JEE jee);

    // DTO → Entity
    @Mapping(source = "studentId", target = "student.studentId")
    JEE toEntity(JEEDTO dto);

    void updateJEEFromDTO(JEEDTO dto, @MappingTarget JEE entity);
}
