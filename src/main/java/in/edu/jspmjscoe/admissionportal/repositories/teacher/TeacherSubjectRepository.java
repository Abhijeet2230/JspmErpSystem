package in.edu.jspmjscoe.admissionportal.repositories.teacher;

import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
}
