package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
    List<StudentExam> findByStudentStudentIdInAndSubjectSubjectIdIn(Collection<Long> studentIds, Collection<Long> subjectIds);

    // ✅ add this
    List<StudentExam> findByStudentStudentId(Long studentId);
}
