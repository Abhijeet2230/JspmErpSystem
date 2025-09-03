package in.edu.jspmjscoe.admissionportal.repositories;

import in.edu.jspmjscoe.admissionportal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByApplicationId(String applicationId);
}
