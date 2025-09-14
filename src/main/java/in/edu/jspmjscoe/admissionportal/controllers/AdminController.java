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
    private final TeacherService teacherService;
    private final DepartmentService departmentService;

    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }
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

    @PostMapping("/add-department")
    public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
        Department savedDepartment = departmentService.saveDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }
    @GetMapping("/getteachers")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable Long id) {
        TeacherDTO teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/saveteacher")
    public ResponseEntity<TeacherDTO> saveTeacher(@RequestBody TeacherDTO teacherDTO) {
        TeacherDTO savedTeacher = teacherService.saveTeacher(teacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher);
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
