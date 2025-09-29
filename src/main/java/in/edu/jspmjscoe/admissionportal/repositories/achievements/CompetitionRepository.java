package in.edu.jspmjscoe.admissionportal.repositories.achievements;

import in.edu.jspmjscoe.admissionportal.model.achievements.Competition;
import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    List<Competition> findByStudentAcademicYear_StudentAcademicYearId(Long studentAcademicYearId);

    List<Competition> findByStudentAcademicYear_Student(Student student);
}
