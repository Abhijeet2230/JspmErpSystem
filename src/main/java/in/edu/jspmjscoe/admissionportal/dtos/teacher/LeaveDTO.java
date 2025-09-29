package in.edu.jspmjscoe.admissionportal.dtos.teacher;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveDTO {
    private Long leaveId;
    private String day;
    private LocalDate date;
    private String reason;
    private LocalTime leaveTime;
    private LocalTime closerTime;
    private LocalTime actualCloserTime;
    private Double leaveHours;
    private String status;
    // Extra fields
    private Long teacherId;
    private String teacherName;
    private String departmentName;
}
