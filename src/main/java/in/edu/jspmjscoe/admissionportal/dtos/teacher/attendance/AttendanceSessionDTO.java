package in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSessionDTO {

    private Long sessionId;

    private String departmentName;
    private String subjectName;
    private String division;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private List<AttendanceStudentDTO> studentAttendances;
}
