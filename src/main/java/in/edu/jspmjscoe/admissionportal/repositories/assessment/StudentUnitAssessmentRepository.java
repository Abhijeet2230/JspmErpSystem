package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StudentUnitAssessmentRepository extends JpaRepository<StudentUnitAssessment, Long> {
//    // used for bulk existence check (faster than single exists calls)
   List<StudentUnitAssessment> findByStudentStudentIdInAndSubjectSubjectIdIn(Collection<Long> studentIds, Collection<Long> subjectIds);

    // (optional) for fetching with student + subject
    @org.springframework.data.jpa.repository.Query("SELECT sua FROM StudentUnitAssessment sua JOIN FETCH sua.student st JOIN FETCH sua.subject subj")
    List<StudentUnitAssessment> findAllWithStudentAndSubject();

    List<StudentUnitAssessment> findByStudentStudentId(long studentId);


}
