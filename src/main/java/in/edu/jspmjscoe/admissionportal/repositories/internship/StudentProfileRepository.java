package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByStudentStudentId(Long studentId);

    boolean existsByStudentStudentId(Long studentId);

    // Newly restored email support
    Optional<StudentProfile> findByEmail(String email);

    boolean existsByEmail(String email);
}


