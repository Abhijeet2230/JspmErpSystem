package in.edu.jspmjscoe.admissionportal.controllers.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.exception.TeacherNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.UserNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.TeacherMapper;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    // ✅ Get the currently logged-in teacher's details
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentTeacher(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 1. Find User from username/email
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));

        // 2. Find teacher linked with that User
        Teacher teacher = teacherRepository.findByUser(user)
                .orElseThrow(() -> new TeacherNotFoundException("User ID: " + user.getUserId()));

        // 3. Map to DTO (department name is already included in mapper)
        TeacherDTO teacherDTO = TeacherMapper.toDTO(teacher);

        return ResponseEntity.ok(teacherDTO);
    }


    // Get teacher by id
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    // Apply for leave
    @PostMapping("/leave/apply")
    public ResponseEntity<LeaveDTO> applyLeave(@RequestBody LeaveDTO leaveDTO) {
        return ResponseEntity.ok(teacherService.applyLeave(leaveDTO));
    }

    // Apply for Head Leave
    @PostMapping("/head-leave/apply")
    public ResponseEntity<HeadLeaveDTO> applyHeadLeave(@RequestBody HeadLeaveDTO headLeaveDTO) {
        return ResponseEntity.ok(teacherService.applyHeadLeave(headLeaveDTO));
    }

}
