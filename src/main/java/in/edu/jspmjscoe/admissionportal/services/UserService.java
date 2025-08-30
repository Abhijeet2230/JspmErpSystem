package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.UserDTO;
import in.edu.jspmjscoe.admissionportal.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

    User findByUsername(String username);
}
