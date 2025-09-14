package in.edu.jspmjscoe.admissionportal.mappers.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.SSCDTO;
import in.edu.jspmjscoe.admissionportal.model.student.SSC;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SSCMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    SSCDTO toDTO(SSC ssc);

    // DTO → Entity
    @Mapping(source = "studentId", target = "student.studentId")
    SSC toEntity(SSCDTO dto);

    void updateSSCFromDTO(SSCDTO dto, @MappingTarget SSC entity);
}
