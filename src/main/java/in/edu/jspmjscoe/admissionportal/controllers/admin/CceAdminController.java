package in.edu.jspmjscoe.admissionportal.controllers.admin;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.*;
import in.edu.jspmjscoe.admissionportal.dtos.subject.SubjectDTO;
import in.edu.jspmjscoe.admissionportal.services.assessment.CceAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cce")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
public class CceAdminController {

    private final CceAdminService cceAdminService;

    // ----------------- Students -----------------
    @GetMapping("/divisions/{division}/students")
    public ResponseEntity<List<StudentCCEDTO>> getStudentsByDivision(@PathVariable String division) {
        List<StudentCCEDTO> students = cceAdminService.getStudentsByDivision(division);
        return ResponseEntity.ok(students);
    }

    // ----------------- Subjects -----------------
    @GetMapping("/divisions/{division}/subjects")
    public ResponseEntity<List<SubjectDTO>> getSubjectsForDivision(@PathVariable String division) {
        List<SubjectDTO> subjects = cceAdminService.getSubjectsForDivision(division);
        return ResponseEntity.ok(subjects);
    }

    // ----------------- Unit Assessments -----------------
    // Get all unit assessments for a subject in a division
    @GetMapping("/divisions/{division}/subjects/{subjectId}/units")
    public ResponseEntity<List<StudentUnitAssessmentDTO>> getUnitAssessments(
            @PathVariable String division,
            @PathVariable Long subjectId) {
        List<StudentUnitAssessmentDTO> units = cceAdminService.getUnitAssessmentsForDivisionAndSubject(division, subjectId);
        return ResponseEntity.ok(units);
    }

    // Update unit marks
    @PatchMapping("/units/{unitId}/update")
    public ResponseEntity<StudentUnitAssessmentDTO> updateUnitMarks(
            @PathVariable Long unitId,
            @RequestParam(required = false) Double quizMarks,
            @RequestParam(required = false) Double activityMarks) {

        StudentUnitAssessmentDTO updated = cceAdminService.updateUnitAssessmentMarks(unitId, quizMarks, activityMarks);
        return ResponseEntity.ok(updated);
    }

    // ----------------- Exams -----------------
    @GetMapping("/divisions/{division}/subjects/{subjectId}/exams")
    public ResponseEntity<List<StudentExamDTO>> getExams(
            @PathVariable String division,
            @PathVariable Long subjectId) {

        List<StudentExamDTO> exams = cceAdminService.getExamsForDivisionAndSubject(division, subjectId);
        return ResponseEntity.ok(exams);
    }

    @PatchMapping("/exams/{examId}/update")
    public ResponseEntity<StudentExamDTO> updateExamMarks(
            @PathVariable Long examId,
            @RequestParam Double marksObtained) {

        StudentExamDTO updated = cceAdminService.updateExamMarks(examId, marksObtained);
        return ResponseEntity.ok(updated);
    }


    // ----------------- Students with Units -----------------
    @GetMapping("/divisions/{division}/subjects/{subjectId}/students-with-units")
    public ResponseEntity<List<StudentWithUnitsDTO>> getStudentsWithUnits(
            @PathVariable String division,
            @PathVariable Long subjectId) {

        List<StudentWithUnitsDTO> data = cceAdminService.getStudentsWithUnits(division, subjectId);
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/units/bulk-update")
    public ResponseEntity<List<StudentUnitAssessmentDTO>> updateMultipleUnits(
            @RequestBody List<UnitUpdateRequestDTO> updates) {

        List<StudentUnitAssessmentDTO> updatedUnits = cceAdminService.updateMultipleUnitAssessments(updates);
        return ResponseEntity.ok(updatedUnits);
    }


    // ✅ Bulk update exam marks
    @PatchMapping("/exams/bulk-update")
    public ResponseEntity<List<StudentExamDTO>> updateExamMarksBulk(
            @RequestBody List<ExamUpdateRequestDTO> requests) {

        List<StudentExamDTO> updatedExams = cceAdminService.updateExamMarksBulk(requests);
        return ResponseEntity.ok(updatedExams);
    }

}
 