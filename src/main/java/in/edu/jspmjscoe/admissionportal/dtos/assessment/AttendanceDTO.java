package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {

    private Long attendanceId;

    private Long studentId;   // only ID reference
    private Long subjectId;   // only ID reference

    private Integer totalClasses;
    private Integer attendedClasses;
}
