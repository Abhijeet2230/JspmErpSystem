package in.edu.jspmjscoe.admissionportal.repositories.student;

import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAcademicYearRepository extends JpaRepository<StudentAcademicYear, Long> {


    List<StudentAcademicYear> findByDivisionAndIsActiveTrue(String division);


    Optional<StudentAcademicYear> findByStudent_StudentIdAndIsActiveTrue(Long studentId);


    // Spring Data will implement this automatically
    List<StudentAcademicYear> findByIsActiveTrue();

}
