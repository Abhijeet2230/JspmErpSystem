package in.edu.jspmjscoe.admissionportal.model.teacher.staffrecord;

import com.fasterxml.jackson.annotation.JsonBackReference;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "staff_monthly_report",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"teacher_id", "year", "month"})
        }
)
public class StaffMonthlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonBackReference
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonBackReference
    private Department department;

    // NEW FIELDS
    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "month", nullable = false)
    private Integer month;

    // existing fields ...
    @Column(name = "days_in_month")
    private Integer daysInMonth;

    @Column(name = "first_third_saturday")
    private Integer firstAndThirdSaturday;

    @Column(name = "sundays")
    private Integer sundays;

    @Column(name = "other_holidays")
    private Integer otherHolidays;

    @Column(name = "actual_working_days")
    private Integer actualWorkingDays;

    @Column(name = "working_on_holiday")
    private Integer workingOnHoliday;

    @Column(name = "casual_leave")
    private Integer cl;

    @Column(name = "comp_off")
    private Integer compOff;

    @Column(name = "medical_leave")
    private Integer ml;

    @Column(name = "earned_leave")
    private Integer el;

    @Column(name = "official_duty_leave")
    private Integer odOrDl;

    @Column(name = "leave_without_pay")
    private Integer lwp;

    @Column(name = "special_leave")
    private Integer specialLeave;

    @Column(name = "gate_pass")
    private Integer gatePass;

    @Column(name = "actual_present_days")
    private Integer actualPresentDays;

    @Column(name = "paid_days")
    private Integer paidDays;

    @Column(name = "present_percentage")
    private Double presentPercentage;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
}
