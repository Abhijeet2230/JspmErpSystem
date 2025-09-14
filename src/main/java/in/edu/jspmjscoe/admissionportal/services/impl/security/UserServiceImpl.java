package in.edu.jspmjscoe.admissionportal.services.impl.security;

import in.edu.jspmjscoe.admissionportal.dtos.security.UserDTO;
import in.edu.jspmjscoe.admissionportal.mappers.security.UserMapper;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toDTO).orElse(null);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            // Update only allowed fields
            user.setUserName(userDTO.getUserName());
            user.setRole(userDTO.getRole());
            user.setEnabled(userDTO.isEnabled());
            user.setFirstLogin(userDTO.isFirstLogin());
            user.setFirstLoginDate(userDTO.getFirstLoginDate());
            user.setLastLoginDate(userDTO.getLastLoginDate());
            return userMapper.toDTO(userRepository.save(user));
        }).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        userRepository.findById(id).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        });
    }
}
