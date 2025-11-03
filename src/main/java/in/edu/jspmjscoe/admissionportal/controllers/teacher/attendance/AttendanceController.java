package in.edu.jspmjscoe.admissionportal.controllers.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceSessionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentMonthlyAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.services.teacher.attendance.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/students")
    public ResponseEntity<List<StudentAttendanceDTO>> getStudentsForAttendance(
            @RequestParam String departmentName,
            @RequestParam String subjectName,
            @RequestParam String division
    ) {
        List<StudentAttendanceDTO> students = attendanceService.getStudentsForAttendance(
                departmentName, subjectName, division
        );
        return ResponseEntity.ok(students);
    }

    @PostMapping("/create")
    public ResponseEntity<AttendanceSessionDTO> createAttendance(@RequestBody AttendanceSessionDTO dto) {
        return ResponseEntity.ok(attendanceService.createAttendanceSession(dto));
    }

    @GetMapping("/subject-attendance")
    public ResponseEntity<List<StudentMonthlyAttendanceDTO>> getSubjectAttendance(
            @RequestParam String subjectName,
            @RequestParam String division,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                attendanceService.getSubjectAttendance(subjectName, division, year)
        );
    }

}
