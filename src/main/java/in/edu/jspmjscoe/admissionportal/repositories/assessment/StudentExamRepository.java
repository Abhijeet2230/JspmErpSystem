package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {

    List<StudentExam> findByStudent_StudentId(Long studentId);

    List<StudentExam> findBySubject_SubjectId(Long subjectId);

    Optional<StudentExam> findByStudent_StudentIdAndSubject_SubjectIdAndExamType(
            Long studentId, Long subjectId, ExamType examType
    );
}
