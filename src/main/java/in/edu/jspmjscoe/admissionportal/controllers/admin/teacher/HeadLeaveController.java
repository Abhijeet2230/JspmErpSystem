package in.edu.jspmjscoe.admissionportal.controllers.admin.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.HeadLeaveRepository;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
public class HeadLeaveController {


    private final TeacherService teacherService;
    private final HeadLeaveRepository headLeaveRepository;


    // ------------------- Head Leave Endpoints -------------------
    // Get pending
    @GetMapping("/get-pending-head-leaves")
    public ResponseEntity<List<HeadLeaveDTO>> getPendingHeadLeaves() {
        return ResponseEntity.ok(teacherService.getPendingHeadLeaves());
    }

    @GetMapping("/pending-head-leaves/count")
    public ResponseEntity<Long> getPendingHeadLeavesCount() {
        long count = headLeaveRepository.countByStatus(Status.PENDING);
        return ResponseEntity.ok(count);
    }

    // Get accepted
    @GetMapping("/get-accepted-head-leaves")
    public ResponseEntity<List<HeadLeaveDTO>> getAcceptedHeadLeaves() {
        return ResponseEntity.ok(teacherService.getAcceptedHeadLeaves());
    }

    // Accept Head Leave
    @PutMapping("/head-leave/{id}/accept")
    public ResponseEntity<HeadLeaveDTO> acceptHeadLeave(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.updateHeadLeaveStatus(id, Status.ACCEPTED));
    }

    // Reject Head Leave
    @PutMapping("/head-leave/{id}/reject")
    public ResponseEntity<HeadLeaveDTO> rejectHeadLeave(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.updateHeadLeaveStatus(id, Status.REJECTED));
    }

}
