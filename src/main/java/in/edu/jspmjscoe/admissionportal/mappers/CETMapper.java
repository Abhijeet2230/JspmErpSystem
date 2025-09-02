package in.edu.jspmjscoe.admissionportal.mappers;

import in.edu.jspmjscoe.admissionportal.dtos.CETDTO;
import in.edu.jspmjscoe.admissionportal.model.CET;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CETMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    CETDTO toDTO(CET cet);

    // DTO → Entity
    @Mapping(source = "studentId", target = "student.studentId")
    CET toEntity(CETDTO dto);

    void updateCETFromDTO(CETDTO dto, @MappingTarget CET entity);
}
