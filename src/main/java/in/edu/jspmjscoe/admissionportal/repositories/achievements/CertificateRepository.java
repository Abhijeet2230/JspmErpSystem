package in.edu.jspmjscoe.admissionportal.repositories.achievements;

import in.edu.jspmjscoe.admissionportal.model.achievements.Certificate;
import in.edu.jspmjscoe.admissionportal.model.achievements.Competition;
import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByStudentAcademicYear_StudentAcademicYearId(Long studentAcademicYearId);

    List<Certificate> findByStudentAcademicYear_Student(Student student);

    List<Certificate> findByStudentAcademicYear(StudentAcademicYear studentAcademicYear);
}
