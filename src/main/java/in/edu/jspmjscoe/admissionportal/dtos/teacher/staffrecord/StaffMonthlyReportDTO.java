package in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffMonthlyReportDTO {

    private Long reportId;
    private TeacherBasicDTO teacher;

    // NEW FIELDS
    private Integer year;
    private Integer month;

    private Integer daysInMonth;
    private Integer firstAndThirdSaturday;
    private Integer sundays;
    private Integer otherHolidays;
    private Integer actualWorkingDays;
    private Integer workingOnHoliday;
    private Integer cl;
    private Integer compOff;
    private Integer ml;
    private Integer el;
    private Integer odOrDl;
    private Integer lwp;
    private Integer specialLeave;
    private Integer gatePass;
    private Integer actualPresentDays;
    private Integer paidDays;
    private Double presentPercentage;
    private String remark;
}
