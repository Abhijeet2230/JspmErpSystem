package in.edu.jspmjscoe.admissionportal.repositories.teacher;

import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
    boolean existsByTeacherAndSubjectAndDivision(Teacher teacher, Subject subject, String division);
}
