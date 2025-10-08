package in.edu.jspmjscoe.admissionportal.repositories.security;

import in.edu.jspmjscoe.admissionportal.model.security.Role;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String username);

    List<User> findByRole(Role teacherRole);
}
