package in.edu.jspmjscoe.admissionportal.controllers.admin;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.CceInitResult;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.StaffMonthlyReportDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.security.UserDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherSubjectDTO;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.staffrecord.StaffMonthlyReportMapper;
import in.edu.jspmjscoe.admissionportal.model.teacher.staffrecord.StaffMonthlyReport;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.HeadLeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.LeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.services.excel.TeacherExcelImportService;
import in.edu.jspmjscoe.admissionportal.services.subject.DepartmentService;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import in.edu.jspmjscoe.admissionportal.services.excel.ExcelImportService;
import in.edu.jspmjscoe.admissionportal.services.impl.assessment.CceInitializationService;
import in.edu.jspmjscoe.admissionportal.services.student.StudentService;
import in.edu.jspmjscoe.admissionportal.services.security.UserService;
import in.edu.jspmjscoe.admissionportal.services.teacher.staffrecord.StaffMonthlyReportService;
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
    private final CceInitializationService cceInitializationService;
    private final TeacherService teacherService;
    private final DepartmentService departmentService;
    private final TeacherRepository teacherRepository;
    private final LeaveRepository leaveRepository;
    private final HeadLeaveRepository headLeaveRepository;
    private final TeacherExcelImportService teacherExcelImportService;
    private final StaffMonthlyReportService staffMonthlyReportService;
    private final StaffMonthlyReportMapper staffMonthlyReportMapper;

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

    @PostMapping("/assign-subject")
    public ResponseEntity<TeacherSubjectDTO> assignSubjectToTeacher(
            @RequestBody TeacherSubjectDTO requestDto) {

        TeacherSubjectDTO tsDto = teacherService.assignSubjectToTeacher(
                requestDto.getTeacherId(),
                requestDto.getSubjectId(),
                requestDto.getDivision()
        );
        return ResponseEntity.ok(tsDto);
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


    // ✅ Upload Excel and Import Students with header row number
    @PostMapping("/import")
    public ResponseEntity<String> importDemoStudents(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid Excel file.");
        }

        // Pass the headerRowNumber to your service
        int importedCount = excelImportService.importStudentsBasic(file);

        return ResponseEntity.ok(importedCount + " students imported successfully.");
    }


    //-----------------Student CC Initializer------------//
    @PostMapping("/initialize")
    public ResponseEntity<CceInitResult> initializeCceData(
            @RequestParam(defaultValue = "true") boolean units,
            @RequestParam(defaultValue = "true") boolean exams) {

        CceInitResult result = cceInitializationService.initializeAll(units, exams);
        return ResponseEntity.ok(result);
    }

    // ------------------- Teacher Excel Import Endpoint -------------------
    @PostMapping("/import-teachers")
    public ResponseEntity<String> importTeachers(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "headerRowNumber", defaultValue = "2") int headerRowNumber) {

        // Basic validations
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid Excel file.");
        }
        String fname = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (!fname.endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body("Only .xlsx files are supported.");
        }

        try {
            int importedCount = teacherExcelImportService.importTeachers(file, headerRowNumber);
            return ResponseEntity.ok(importedCount + " teachers imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import teachers: " + e.getMessage());
        }
    }


}
