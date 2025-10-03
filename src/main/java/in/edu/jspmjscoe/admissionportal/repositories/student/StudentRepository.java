package in.edu.jspmjscoe.admissionportal.repositories.student;

import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s JOIN s.studentAcademicYears ay " +
            "WHERE ay.course = :course AND ay.semester = :semester AND ay.division = :division AND ay.isActive = true")
    List<Student> findStudentsByCourseSemesterDivision(
            @Param("course") Course course,
            @Param("semester") Integer semester,
            @Param("division") String division
    );


    Optional<Student> findByApplicationId(String applicationId);

    Optional<Student> findByUser(User user);

}
