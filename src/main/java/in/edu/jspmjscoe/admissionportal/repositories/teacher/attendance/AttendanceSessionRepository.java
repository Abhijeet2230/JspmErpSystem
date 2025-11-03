package in.edu.jspmjscoe.admissionportal.repositories.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.attendance.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {

    // Fetch all sessions for a teacher
    List<AttendanceSession> findByTeacher_TeacherId(Long teacherId);

    // Fetch all sessions for a teacher for a particular date
    List<AttendanceSession> findByTeacher_TeacherIdAndDate(Long teacherId, LocalDate date);

    List<AttendanceSession> findBySubject_NameAndDivisionAndDate(String subjectName, String division, LocalDate date);

    @Query("""
    SELECT COUNT(a) > 0
    FROM AttendanceSession a
    WHERE a.teacher = :teacher
      AND a.subject = :subject
      AND a.date = :date
      AND a.division = :division
      AND a.startTime = :startTime
      AND a.endTime = :endTime
""")
    boolean existsDuplicateSession(Teacher teacher, Subject subject, LocalDate date, String division,
                                   LocalTime startTime, LocalTime endTime);
}
