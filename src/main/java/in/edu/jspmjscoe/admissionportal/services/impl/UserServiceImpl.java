package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.UserDTO;
import in.edu.jspmjscoe.admissionportal.model.User;
import in.edu.jspmjscoe.admissionportal.repository.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static in.edu.jspmjscoe.admissionportal.mapper.UserMapper.convertToDto;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return convertToDto(user);
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }
}
