package in.edu.jspmjscoe.admissionportal.repositories.teacher;

import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.teacher.HeadLeave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeadLeaveRepository extends JpaRepository<HeadLeave, Long> {
    List<HeadLeave> findByTeacher_TeacherId(Long teacherId);
    List<HeadLeave> findByStatus(Status status);
    boolean existsByTeacher_TeacherIdAndStatus(Long teacherId, Status status);
    long countByStatus(Status status);
}
