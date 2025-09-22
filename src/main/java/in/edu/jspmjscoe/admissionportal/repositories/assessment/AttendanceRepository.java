package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentStudentIdInAndSubjectSubjectIdIn(Collection<Long> studentIds, Collection<Long> subjectIds);

    List<Attendance> findByStudentStudentId(long studentId);
}