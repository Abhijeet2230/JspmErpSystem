package in.edu.jspmjscoe.admissionportal.controllers.admin.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.HeadLeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.LeaveRepository;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class LeaveController {

    private final TeacherService  teacherService;
    private final LeaveRepository  leaveRepository;
    private final HeadLeaveRepository headLeaveRepository;

    // ------------------- Teacher Leave Endpoints -------------------
    // Get leave
    @GetMapping("/get-pending-leaves")
    public ResponseEntity<List<LeaveDTO>> getAllLeaves() {
        return ResponseEntity.ok(teacherService.getPendingLeaves());
    }

    @PostMapping("/leave/{id}/end")
    public ResponseEntity<LeaveDTO> endLeaveByAdmin(
            @PathVariable Long id,
            @RequestParam LocalTime actualCloserTime) {
        return ResponseEntity.ok(teacherService.endLeaveEarly(id, actualCloserTime));
    }

    @GetMapping("/pending-leaves/count")
    public ResponseEntity<Long> getPendingLeavesCount() {
        long count = leaveRepository.countByStatus(Status.PENDING);
        return ResponseEntity.ok(count);
    }

    // ✅ Get all accepted leaves
    @GetMapping("/get-accepted-leaves")
    public ResponseEntity<List<LeaveDTO>> getAllAcceptedLeaves() {
        return ResponseEntity.ok(teacherService.getAcceptedLeaves());
    }

    // ✅ Accept Leave
    @PutMapping("/leave/{id}/accept")
    public ResponseEntity<LeaveDTO> acceptLeave(@PathVariable Long id) {
        LeaveDTO leave = teacherService.updateLeaveStatus(id, Status.ACCEPTED);
        return ResponseEntity.ok(leave);
    }

    // ✅ Reject Leave
    @PutMapping("/leave/{id}/reject")
    public ResponseEntity<LeaveDTO> rejectLeave(@PathVariable Long id) {
        LeaveDTO leave = teacherService.updateLeaveStatus(id, Status.REJECTED);
        return ResponseEntity.ok(leave);
    }
}
