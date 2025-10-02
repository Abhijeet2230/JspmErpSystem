package in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkStaffUpdateDTO {
        private Long teacherId;   // identify teacher
        private Integer year;     // identify year
        private Integer month;    // identify month

        // only update-able fields
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
