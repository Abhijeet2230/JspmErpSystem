package in.edu.jspmjscoe.admissionportal.mappers.security;

import in.edu.jspmjscoe.admissionportal.dtos.security.UserDTO;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

        @Mapping(source = "student.studentId", target = "studentId")
        @Mapping(source = "teacher.teacherId", target = "teacherId")
        UserDTO toDTO(User user);

        @InheritInverseConfiguration
        User toEntity(UserDTO dto);
}
