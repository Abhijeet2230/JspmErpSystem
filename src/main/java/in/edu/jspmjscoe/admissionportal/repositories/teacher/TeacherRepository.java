package in.edu.jspmjscoe.admissionportal.repositories.teacher;


import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUser_UserId(Long userId);

    // ✅ New method to check directly by User entity
    Optional<Teacher> findByUser(User user);
    List<Teacher> findByStatus(Status status);
    Optional<Teacher> findById(Long id);
}
