package in.edu.jspmjscoe.admissionportal.controllers.admin;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.CceInitResult;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.security.UserDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.HeadLeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.LeaveRepository;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import in.edu.jspmjscoe.admissionportal.services.excel.ExcelImportService;
import in.edu.jspmjscoe.admissionportal.services.impl.assessment.CceInitializationService;
import in.edu.jspmjscoe.admissionportal.services.student.StudentService;
import in.edu.jspmjscoe.admissionportal.services.security.UserService;
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
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "headerRowNumber", defaultValue = "1") int headerRowNumber) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid Excel file.");
        }

        // Pass the headerRowNumber to your service
        int importedCount = excelImportService.importStudentsBasic(file, headerRowNumber);

        return ResponseEntity.ok(importedCount + " students imported successfully.");
    }



    @PostMapping("/initialize")
    public ResponseEntity<CceInitResult> initializeCceData(
            @RequestParam(defaultValue = "true") boolean units,
            @RequestParam(defaultValue = "true") boolean exams) {

        CceInitResult result = cceInitializationService.initializeAll(units, exams);
        return ResponseEntity.ok(result);
    }


}
