package in.edu.jspmjscoe.admissionportal.dtos.teacher;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeadLeaveDTO {
    private Long headLeaveId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int numberOfDays;
    private String reason;
    private String prefixSuffix;
    private String substituteTeacherName;
    private String status;

    // Extra fields
    private Long teacherId;
    private String teacherName;
    private String departmentName;
}
