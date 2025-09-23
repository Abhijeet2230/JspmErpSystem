package in.edu.jspmjscoe.admissionportal.repositories.teacher;


import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.TeacherBasicDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUser_UserId(Long userId);

    // ✅ New method to check directly by User entity
    Optional<Teacher> findByUser(User user);
    List<Teacher> findByStatus(Status status);
    Optional<Teacher> findById(Long id);

    Optional<Teacher> findByEmployeeId(String employeeId);
    Optional<Teacher> findByOfficialEmail(String officialEmail);
    Optional<Teacher> findByAadhaarNumber(String aadhaar);

    // Projection query to fetch only needed fields
    @Query("SELECT new in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.TeacherBasicDTO(" +
            "t.teacherId, CONCAT(t.firstName, ' ', t.lastName), t.designation, d.departmentId, d.name) " +
            "FROM Teacher t JOIN t.department d WHERE t.status = 'ACTIVE'")
    List<TeacherBasicDTO> findAllTeacherBasics();
}
