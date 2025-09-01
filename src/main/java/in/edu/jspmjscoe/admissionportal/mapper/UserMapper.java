package in.edu.jspmjscoe.admissionportal.mapper;

import in.edu.jspmjscoe.admissionportal.dtos.UserDTO;
import in.edu.jspmjscoe.admissionportal.model.User;

public class UserMapper {

    public static UserDTO convertToDto(User user) {
        UserDTO dto = new UserDTO();

        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setFirstLogin(user.isFirstLogin());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setFirstLoginDate(user.getFirstLoginDate());
        dto.setLastLoginDate(user.getLastLoginDate());
        dto.setStudentId(user.getStudent() != null ? user.getStudent().getStudentId() : null);

        return dto;
    }
}
