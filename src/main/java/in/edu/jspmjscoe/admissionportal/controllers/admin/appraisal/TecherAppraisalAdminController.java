package in.edu.jspmjscoe.admissionportal.controllers.admin.appraisal;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalMarksDTO;
import in.edu.jspmjscoe.admissionportal.services.teacher.appraisal.TeacherAppraisalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/appraisals")
@RequiredArgsConstructor
public class TecherAppraisalAdminController {

    private final TeacherAppraisalService teacherAppraisalService;

    @GetMapping("/{id}")
    public ResponseEntity<TeacherAppraisalDTO> getAppraisal(@PathVariable Long id) {
        TeacherAppraisalDTO dto = teacherAppraisalService.getAppraisalById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeacherAppraisalDTO>> getAllAppraisals() {
        List<TeacherAppraisalDTO> appraisals = teacherAppraisalService.getAllAppraisals();
        return ResponseEntity.ok(appraisals);
    }

    // Bulk update marks
    @PutMapping("/marks/bulk")
    public ResponseEntity<List<TeacherAppraisalDTO>> bulkUpdateMarks(
            @RequestBody List<TeacherAppraisalMarksDTO> marksList
    ) {
        List<TeacherAppraisalDTO> updatedAppraisals = teacherAppraisalService.bulkUpdateMarks(marksList);
        return ResponseEntity.ok(updatedAppraisals);
    }
}
