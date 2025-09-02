package in.edu.jspmjscoe.admissionportal.mappers;

import in.edu.jspmjscoe.admissionportal.dtos.AdmissionDTO;
import in.edu.jspmjscoe.admissionportal.model.Admission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdmissionMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    AdmissionDTO toDTO(Admission admission);

    // DTO → Entity
    @Mapping(source = "studentId", target = "student.studentId")
    Admission toEntity(AdmissionDTO dto);


    void updateAdmissionFromDTO(AdmissionDTO dto, @MappingTarget Admission entity);
}
