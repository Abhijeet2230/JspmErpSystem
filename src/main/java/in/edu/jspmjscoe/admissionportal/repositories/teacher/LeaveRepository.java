package in.edu.jspmjscoe.admissionportal.repositories.teacher;

import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.teacher.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByTeacher_TeacherId(Long teacherId);
    List<Leave> findByStatus(Status status);
    boolean existsByTeacher_TeacherIdAndStatus(Long teacherId, Status status);

    long countByStatus(Status status);
}
