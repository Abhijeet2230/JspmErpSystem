package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentUnitAssessmentRepository extends JpaRepository<StudentUnitAssessment, Long> {

    List<StudentUnitAssessment> findByStudent_StudentId(Long studentId);

    List<StudentUnitAssessment> findBySubject_SubjectId(Long subjectId);

    List<StudentUnitAssessment> findByStudent_StudentIdAndSubject_SubjectId(Long studentId, Long subjectId);
}
