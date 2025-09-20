package in.edu.jspmjscoe.admissionportal.services.security;

import in.edu.jspmjscoe.admissionportal.dtos.security.UserDTO;
import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
}
