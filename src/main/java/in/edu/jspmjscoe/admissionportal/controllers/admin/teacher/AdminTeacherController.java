package in.edu.jspmjscoe.admissionportal.controllers.admin.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.LeaveRepository;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/teacher")
public class AdminTeacherController {

    private final LeaveRepository leaveRepository;
    private final TeacherService teacherService;

    // ------------------- Teacher Endpoints -------------------

    @GetMapping("/get-accepted-teachers")
    public ResponseEntity<List<TeacherDTO>> getAllAcceptedTeachers() {
        return ResponseEntity.ok(teacherService.getAcceptedTeachers());
    }

    @GetMapping("/get-pending-teachers")
    public ResponseEntity<List<TeacherDTO>> getAllPendingTeachers() {
        return ResponseEntity.ok(teacherService.getPendingTeachers());
    }

    @GetMapping("/view-teachers-details/{id}")
    public ResponseEntity<TeacherDTO> viewTeacherDetails(@PathVariable Long id) {
        TeacherDTO teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(teacher);
    }

    // ✅ Accept Teacher
    @PutMapping("/teacher/{id}/accept")
    public ResponseEntity<TeacherDTO> acceptTeacher(@PathVariable Long id) {
        TeacherDTO teacher = teacherService.updateTeacherStatus(id, Status.ACCEPTED);
        return ResponseEntity.ok(teacher);
    }

    // ✅ Reject Teacher
    @PutMapping("/teacher/{id}/reject")
    public ResponseEntity<TeacherDTO> rejectTeacher(@PathVariable Long id) {
        TeacherDTO teacher = teacherService.updateTeacherStatus(id, Status.REJECTED);
        return ResponseEntity.ok(teacher);
    }

}
