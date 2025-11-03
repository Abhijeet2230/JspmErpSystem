package in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentMonthlyAttendanceDTO {

    private Long studentId;
    private String studentName;
    private String rollNo;
    private String division;
    private String subjectName;

    private Map<String, Integer> attendanceByDate; // date (String) -> 1 (Present) or 0 (Absent)
    private int totalSessions;
    private int presentSessions;

    private int totalDays;
    private int presentDays;
    private Double percentage;
}
