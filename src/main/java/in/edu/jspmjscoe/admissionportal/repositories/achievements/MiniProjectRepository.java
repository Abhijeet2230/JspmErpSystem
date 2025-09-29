package in.edu.jspmjscoe.admissionportal.repositories.achievements;

import in.edu.jspmjscoe.admissionportal.model.achievements.MiniProject;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiniProjectRepository extends JpaRepository<MiniProject, Long> {

    // Example: find all mini projects by a student's academic year ID
    List<MiniProject> findByStudentAcademicYear_StudentAcademicYearId(Long studentAcademicYearId);

    // add more custom queries if needed
    List<MiniProject> findByStudentAcademicYear_Student(Student student);

    List<MiniProject> findByStudentAcademicYear(StudentAcademicYear studentAcademicYear);
}
