package in.edu.jspmjscoe.admissionportal.repositories.achievements;

import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import in.edu.jspmjscoe.admissionportal.model.achievements.Internship;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {

    List<Internship> findByStudentAcademicYear_StudentAcademicYearId(Long studentAcademicYearId);

    List<Internship> findByStudentAcademicYear_Student(Student student);

    List<Internship> findByStudentAcademicYear(StudentAcademicYear studentAcademicYear);
}
