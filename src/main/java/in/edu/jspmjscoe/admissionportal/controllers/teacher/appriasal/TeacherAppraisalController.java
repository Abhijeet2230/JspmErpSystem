package in.edu.jspmjscoe.admissionportal.controllers.teacher.appriasal;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/teacher/appraisals")
@RequiredArgsConstructor
public class TeacherAppraisalController {

    private final TeacherService appraisalService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<TeacherAppraisalDTO> createAppraisal(
            @RequestPart("data") TeacherAppraisalDTO dto,
            @RequestPart(value = "document", required = false) MultipartFile document,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "video", required = false) MultipartFile video
    ) {
        // Example: save files to disk/cloud, and set paths into DTO
        if (document != null) {
            dto.setAppraisalDocumentPath("/uploads/docs/" + document.getOriginalFilename());
        }
        if (photo != null) {
            dto.setActivityPhotoPath("/uploads/photos/" + photo.getOriginalFilename());
        }
        if (video != null) {
            dto.setActivityVideoPath("/uploads/videos/" + video.getOriginalFilename());
        }

        return ResponseEntity.ok(appraisalService.createAppraisal(dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<TeacherAppraisalDTO> getAppraisal(@PathVariable Long id) {
        return ResponseEntity.ok(appraisalService.getAppraisalById(id));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherAppraisalDTO>> getAppraisalsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(appraisalService.getAppraisalsByTeacher(teacherId));
    }

    @GetMapping
    public ResponseEntity<List<TeacherAppraisalDTO>> getAllAppraisals() {
        return ResponseEntity.ok(appraisalService.getAllAppraisals());
    }
}
