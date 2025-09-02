package in.edu.jspmjscoe.admissionportal.mappers;

import in.edu.jspmjscoe.admissionportal.dtos.HSCDTO;
import in.edu.jspmjscoe.admissionportal.model.HSC;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HSCMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    HSCDTO toDTO(HSC hsc);

    // DTO → Entity
    @Mapping(source = "studentId", target = "student.studentId")
    HSC toEntity(HSCDTO dto);

    void updateHSCFromDTO(HSCDTO dto, @MappingTarget HSC entity);

}
