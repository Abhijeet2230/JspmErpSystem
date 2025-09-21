package in.edu.jspmjscoe.admissionportal.controllers.student;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.StudentCCEProfileResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.security.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.StudentPlacementDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.UnauthorizedException;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.assessment.StudentCCEProfileService;
import in.edu.jspmjscoe.admissionportal.services.student.StudentService;
import in.edu.jspmjscoe.admissionportal.services.trainingplacement.TrainingPlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentService studentService;
    private final StudentCCEProfileService studentCCEProfileService; // ✅ Inject service
    private final TrainingPlacementService trainingPlacementService;


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentStudent(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized: Login required");
        }

        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        return ResponseEntity.ok(student);
    }

    // ✅ Change password
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

    @GetMapping("/cce")
    public ResponseEntity<?> getCCEProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized: Login required");
        }

        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        StudentCCEProfileResponseDTO profile = studentCCEProfileService.getStudentCCEProfile(student.getStudentId());
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/training-placement")
    public StudentPlacementDTO getMyTrainingPlacement(@AuthenticationPrincipal(expression = "id") Long studentId) {
        return trainingPlacementService.getMyTrainingPlacement(studentId);
    }
}
