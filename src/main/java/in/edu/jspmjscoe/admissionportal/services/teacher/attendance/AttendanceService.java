package in.edu.jspmjscoe.admissionportal.services.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceSessionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentAttendanceDTO;

import java.util.List;

public interface AttendanceService {
    AttendanceSessionDTO createAttendanceSession(AttendanceSessionDTO sessionDTO);
    AttendanceSessionDTO getAttendanceSession(Long sessionId);
    List<StudentAttendanceDTO> getStudentsForAttendance(
            String departmentName,
            String courseName,
            Integer semester,
            String subjectName,
            String division
    );
}
