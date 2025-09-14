package in.edu.jspmjscoe.admissionportal.repositories.subject;

import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
