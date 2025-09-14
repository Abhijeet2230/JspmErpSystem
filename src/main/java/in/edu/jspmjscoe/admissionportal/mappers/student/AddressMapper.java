package in.edu.jspmjscoe.admissionportal.mappers.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.AddressDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    AddressDTO toDTO(Address address);

    // DTO → Entity
    @Mapping(source = "studentId", target = "student.studentId")
    Address toEntity(AddressDTO dto);

    void updateAddressFromDTO(AddressDTO dto, @MappingTarget Address entity);
}
