package in.edu.jspmjscoe.admissionportal.repositories.student;

import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByApplicationId(String applicationId);

    Optional<Student> findByUser(User user);

    // (Optional) If you want by userId directly
    Optional<Student> findByUser_UserId(Long userId);

    // (Optional) If you want by username directly
    Optional<Student> findByUser_UserName(String userName);
}
