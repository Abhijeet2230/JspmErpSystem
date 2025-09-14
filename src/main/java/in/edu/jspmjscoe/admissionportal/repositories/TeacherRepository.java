package in.edu.jspmjscoe.admissionportal.repositories;

import in.edu.jspmjscoe.admissionportal.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
