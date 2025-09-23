package in.edu.jspmjscoe.admissionportal.repositories.student;

import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAcademicYearRepository extends JpaRepository<StudentAcademicYear, Long> {

    // 🔹 Find all records for a specific student
    List<StudentAcademicYear> findByStudentStudentId(Long studentId);

    // 🔹 Find the currently active academic year for a student
    StudentAcademicYear findByStudentStudentIdAndIsActiveTrue(Long studentId);

    List<StudentAcademicYear> findByDivisionAndIsActiveTrue(String division);
    List<StudentAcademicYear> findByStudent_StudentId(Long studentId);

    Optional<StudentAcademicYear> findByStudent_StudentIdAndIsActiveTrue(Long studentId);

    // Spring Data will implement this automatically
    List<StudentAcademicYear> findByIsActiveTrue();

    // 🔹 Find by year + semester
    List<StudentAcademicYear> findByYearOfStudyAndSemester(Integer yearOfStudy, Integer semester);
}
