package in.edu.jspmjscoe.admissionportal.controllers.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceSessionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentAttendanceDTO;
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
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String division
    ) {
        List<StudentAttendanceDTO> students = attendanceService.getStudentsForAttendance(
                departmentName, courseName, semester, subjectName, division
        );
        return ResponseEntity.ok(students);
    }


    @PostMapping("/create")
    public ResponseEntity<AttendanceSessionDTO> createAttendance(@RequestBody AttendanceSessionDTO dto) {
        return ResponseEntity.ok(attendanceService.createAttendanceSession(dto));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<AttendanceSessionDTO> getAttendance(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getAttendanceSession(sessionId));
    }
}
