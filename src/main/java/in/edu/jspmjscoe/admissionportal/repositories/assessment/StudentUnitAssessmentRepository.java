package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StudentUnitAssessmentRepository extends JpaRepository<StudentUnitAssessment, Long> {

    // 🔹 Find all assessments for a studentAcademicYear
    List<StudentUnitAssessment> findByStudentAcademicYearStudentAcademicYearId(Long studentAcademicYearId);

    // 🔹 Find all assessments for multiple studentAcademicYears (batch fetch)
    List<StudentUnitAssessment> findByStudentAcademicYearStudentAcademicYearIdIn(List<Long> studentAcademicYearIds);

    // 🔹 Find assessments by subject
    List<StudentUnitAssessment> findBySubjectSubjectId(Long subjectId);

    // 🔹 Find specific unit assessment
    StudentUnitAssessment findByStudentAcademicYearStudentAcademicYearIdAndSubjectSubjectIdAndUnitNumber(
            Long studentAcademicYearId, Long subjectId, Integer unitNumber
    );

    Optional<StudentUnitAssessment> findByStudentAcademicYear_StudentAcademicYearIdAndSubject_SubjectIdAndUnitNumber(
            Long studentAcademicYearId,
            Long subjectId,
            Integer unitNumber
    );


    List<StudentUnitAssessment> findByStudentAcademicYearStudentAcademicYearIdInAndSubjectSubjectIdIn(
            Collection<Long> studentAcademicYearIds,
            Collection<Long> subjectIds
    );



}
