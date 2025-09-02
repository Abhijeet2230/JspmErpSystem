package in.edu.jspmjscoe.admissionportal.mappers;

import in.edu.jspmjscoe.admissionportal.dtos.UserDTO;
import in.edu.jspmjscoe.admissionportal.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

        @Mapping(source = "student.studentId", target = "studentId")
        UserDTO toDTO(User user);

        @InheritInverseConfiguration
        User toEntity(UserDTO dto);

}
