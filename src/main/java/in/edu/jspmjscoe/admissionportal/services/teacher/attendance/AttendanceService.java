package in.edu.jspmjscoe.admissionportal.services.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AdminStudentSubjectAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceSessionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentMonthlyAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.model.teacher.attendance.AttendanceSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    AttendanceSessionDTO createAttendanceSession(AttendanceSessionDTO sessionDTO);


    List<StudentAttendanceDTO> getStudentsForAttendance(
            String departmentName,
            String subjectName,
            String division
    );
    List<StudentMonthlyAttendanceDTO> getMonthlyAttendance(String subjectName, String division, int year, int month);

    List<AttendanceSessionDTO> getAttendanceSessionsByFilter(String subjectName, String division, LocalDate date);
    List<AdminStudentSubjectAttendanceDTO> getMonthlySubjectWiseAttendanceForAdmin(String division, int year, int month);
}
