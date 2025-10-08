package in.edu.jspmjscoe.admissionportal.controllers.student;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.StudentCCEProfileResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.security.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.dtos.subject.SubjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentOverallSubjectAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.StudentPlacementDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.security.UnauthorizedException;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.assessment.CceAdminService;
import in.edu.jspmjscoe.admissionportal.services.assessment.StudentCCEProfileService;
import in.edu.jspmjscoe.admissionportal.services.student.StudentService;
import in.edu.jspmjscoe.admissionportal.services.trainingplacement.TrainingPlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentService studentService;
    private final StudentCCEProfileService studentCCEProfileService;
    private final TrainingPlacementService trainingPlacementService;
    private final CceAdminService cceAdminService;

    

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
    public ResponseEntity<?> getMyTrainingPlacement(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized: Login required");
        }

        // Find the logged-in user
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Find the student profile linked to the user
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        // Call service with studentId
        StudentPlacementDTO placement = trainingPlacementService.getMyTrainingPlacement(student.getStudentId());

        return ResponseEntity.ok(placement);
    }


    // ----------------- Subjects for Logged In Student -----------------
    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectDTO>> getSubjectsForLoggedInStudent(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<SubjectDTO> subjects = cceAdminService.getSubjectsForLoggedInStudent(userDetails);
        return ResponseEntity.ok(subjects);
    }

    // ---------------- Student Attendance ----------------------//
    @GetMapping("/attendance-overall")
    public ResponseEntity<List<StudentOverallSubjectAttendanceDTO>> getOverallAttendanceForLoggedInStudent(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized: Login required");
        }

        List<StudentOverallSubjectAttendanceDTO> data =
                studentService.getOverallAttendanceForStudent(userDetails.getUsername());

        return ResponseEntity.ok(data);
    }

}
