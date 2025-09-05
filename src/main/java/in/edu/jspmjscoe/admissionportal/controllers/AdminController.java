package in.edu.jspmjscoe.admissionportal.controllers;

import in.edu.jspmjscoe.admissionportal.dtos.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.UserDTO;
import in.edu.jspmjscoe.admissionportal.model.Student;
import in.edu.jspmjscoe.admissionportal.services.ExcelImportService;
import in.edu.jspmjscoe.admissionportal.services.StudentService;
import in.edu.jspmjscoe.admissionportal.services.UserService;
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
}
