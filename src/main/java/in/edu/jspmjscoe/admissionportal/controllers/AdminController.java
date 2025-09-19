package in.edu.jspmjscoe.admissionportal.controllers;


import in.edu.jspmjscoe.admissionportal.dtos.security.UserDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.HeadLeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.LeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.services.subject.DepartmentService;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import in.edu.jspmjscoe.admissionportal.services.excel.ExcelImportService;
import in.edu.jspmjscoe.admissionportal.services.security.UserService;
import in.edu.jspmjscoe.admissionportal.services.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final ExcelImportService excelImportService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final DepartmentService departmentService;
    private final TeacherRepository teacherRepository;
    private final LeaveRepository leaveRepository;
    private final HeadLeaveRepository headLeaveRepository;

    // ------------------- User Endpoints -------------------

    @GetMapping("/getusers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getstudents")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
         List<StudentDTO> student = studentService.getAllStudents();
        return ResponseEntity.ok(student);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

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

    // ------------------- Teacher Leave Endpoints -------------------
    // Get leave
    @GetMapping("/get-pending-leaves")
    public ResponseEntity<List<LeaveDTO>> getAllLeaves() {
        return ResponseEntity.ok(teacherService.getPendingLeaves());
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



    // ------------------- Excel Import Endpoint -------------------

    @PostMapping("/import-students")
    public ResponseEntity<String> importStudents(@RequestParam("file") MultipartFile file) {

        // Validate file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid Excel file.");
        }

        // Optional: check file type
        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body("Only .xlsx files are supported.");
        }

        try {
            int importedCount = excelImportService.importStudents(file);
            return ResponseEntity.ok("Successfully imported " + importedCount + " students.");
        } catch (Exception e) {
            // Detailed error message for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import students: " + e.getMessage());
        }
    }



}
