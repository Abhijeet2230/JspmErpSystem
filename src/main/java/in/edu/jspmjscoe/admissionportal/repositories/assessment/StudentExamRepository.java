package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface StudentExamRepository extends JpaRepository<StudentExam, Long> { ;

    List<StudentExam> findByStudentAcademicYearStudentAcademicYearId(Long studentAcademicYearId);

    List<StudentExam> findByStudentAcademicYearStudentAcademicYearIdInAndSubjectSubjectIdIn(
            Collection<Long> studentAcademicYearIds,
            Collection<Long> subjectIds
    );


}
