package in.edu.jspmjscoe.admissionportal.controllers.admin.staffReport;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.StaffMonthlyReportDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.TeacherBasicDTO;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.staffrecord.StaffMonthlyReportMapper;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.staffrecord.StaffMonthlyReport;

import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import in.edu.jspmjscoe.admissionportal.services.teacher.staffrecord.StaffMonthlyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/api/admin/staff")
public class StaffReportController {

    private final StaffMonthlyReportMapper staffMonthlyReportMapper;
    private final StaffMonthlyReportService staffMonthlyReportService;
    private final TeacherService teacherService;
    private final TeacherRepository teacherRepository;

    // ----------------- Fetch basic teacher info -----------------
    @GetMapping("/admin/teachers/basic")
    public ResponseEntity<List<TeacherBasicDTO>> getAllTeachersBasic() {
        List<TeacherBasicDTO> teacherBasics = teacherRepository.findAllTeacherBasics();
        return ResponseEntity.ok(teacherBasics);
    }

    // ----------------- Generate monthly staff report for frontend table -----------------
    @GetMapping("/generate-monthly-report")
    public ResponseEntity<List<StaffMonthlyReportDTO>> generateMonthlyReport(
            @RequestParam int year,
            @RequestParam int month) {

        List<StaffMonthlyReportDTO> reports = staffMonthlyReportService.generateMonthlyReports(year, month);
        return ResponseEntity.ok(reports);
    }

    // ----------------- Bulk Update for Staff Record -----------------
    @PutMapping("/bulk-update")
    public ResponseEntity<String> bulkUpdate(@RequestBody List<StaffMonthlyReportDTO> dtos) {
        List<StaffMonthlyReport> entities = dtos.stream()
                .map(staffMonthlyReportMapper::toEntity)
                .collect(Collectors.toList());
        staffMonthlyReportService.bulkUpdate(entities);
        return ResponseEntity.ok("✅ Bulk update completed successfully!");
    }

    // ----------------- Fetch all reports for a teacher -----------------
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<StaffMonthlyReportDTO>> getReportsByTeacher(@PathVariable Long teacherId) {
        List<StaffMonthlyReport> reports = staffMonthlyReportService.getReportsByTeacher(teacherId);
        List<StaffMonthlyReportDTO> dtos = reports.stream()
                .map(staffMonthlyReportMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ----------------- Fetch all reports for a department -----------------
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StaffMonthlyReportDTO>> getReportsByDepartment(@PathVariable Long departmentId) {
        List<StaffMonthlyReport> reports = staffMonthlyReportService.getReportsByDepartment(departmentId);
        List<StaffMonthlyReportDTO> dtos = reports.stream()
                .map(staffMonthlyReportMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}

