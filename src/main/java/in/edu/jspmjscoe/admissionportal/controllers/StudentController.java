package in.edu.jspmjscoe.admissionportal.controllers;


import in.edu.jspmjscoe.admissionportal.dtos.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.model.Student;
import in.edu.jspmjscoe.admissionportal.model.User;
import in.edu.jspmjscoe.admissionportal.repositories.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentService studentService;


    // ✅ Get the currently logged-in student's details
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentStudent(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 1. Find User from username/email
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Find Student linked with that User
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        return ResponseEntity.ok(student);
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChangePasswordRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            studentService.changePassword(userDetails.getUsername(), request);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Could not change password");
        }
    }






}
