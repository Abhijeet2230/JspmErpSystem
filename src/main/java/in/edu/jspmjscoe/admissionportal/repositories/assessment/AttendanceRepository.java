package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudent_StudentIdAndSubject_SubjectId(Long studentId, Long subjectId);

    List<Attendance> findByStudent_StudentId(Long studentId);

    List<Attendance> findBySubject_SubjectId(Long subjectId);
}
