package in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSessionDTO {

    private Long sessionId;

    // Frontend passes names only
    private String departmentName;
    private String courseName;
    private Integer semester;
    private String subjectName;
    private String division;
    private LocalDate date;
    private Integer periodNumber;

    private List<AttendanceStudentDTO> studentAttendances;
}
