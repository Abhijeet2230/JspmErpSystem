package in.edu.jspmjscoe.admissionportal.repositories.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.model.teacher.attendance.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {

    // Fetch all sessions for a teacher
    List<AttendanceSession> findByTeacher_TeacherId(Long teacherId);

    // Fetch all sessions for a teacher for a particular date
    List<AttendanceSession> findByTeacher_TeacherIdAndDate(Long teacherId, LocalDate date);

    List<AttendanceSession> findBySubject_NameAndDivisionAndDate(String subjectName, String division, LocalDate date);


}
