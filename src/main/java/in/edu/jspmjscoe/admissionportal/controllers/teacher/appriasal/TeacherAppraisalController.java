package in.edu.jspmjscoe.admissionportal.controllers.teacher.appriasal;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import in.edu.jspmjscoe.admissionportal.services.teacher.appraisal.TeacherAppraisalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher/appraisals")
@RequiredArgsConstructor
public class TeacherAppraisalController {

    private final TeacherAppraisalService teacherAppraisalService;

    // -------------------- CREATE APPRAISAL --------------------
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<TeacherAppraisalDTO> createAppraisal(
            @RequestPart("data") TeacherAppraisalDTO dto,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(value = "document", required = false) MultipartFile document,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "video", required = false) MultipartFile video
    ) {
        TeacherAppraisalDTO saved = teacherAppraisalService.createAppraisal(dto,userDetails,document, photo, video);
        return ResponseEntity.ok(saved);
    }

    // -------------------- GET APPRAISALS BY TEACHER --------------------
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherAppraisalDTO>> getAppraisalsByTeacher(@PathVariable Long teacherId) {
        List<TeacherAppraisalDTO> dtos = teacherAppraisalService.getAppraisalsByTeacher(teacherId);
        return ResponseEntity.ok(dtos);
    }

}
