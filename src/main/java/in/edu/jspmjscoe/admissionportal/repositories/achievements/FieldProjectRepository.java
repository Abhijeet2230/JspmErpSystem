package in.edu.jspmjscoe.admissionportal.repositories.achievements;

import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldProjectRepository extends JpaRepository<FieldProject, Long> {

    // Example: find all field projects by a student's academic year ID
    List<FieldProject> findByStudentAcademicYear_StudentAcademicYearId(Long studentAcademicYearId);

    // add more custom queries if needed


    List<FieldProject> findByStudentAcademicYear_Student(Student student);
}
