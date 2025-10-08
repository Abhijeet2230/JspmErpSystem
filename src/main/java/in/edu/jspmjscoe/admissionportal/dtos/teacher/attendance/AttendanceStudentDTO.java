package in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceStudentDTO {
    private Long attendanceId;
    private Long studentId;
    private String studentName;
    private String status;
}
