package in.edu.jspmjscoe.admissionportal.model.teacher;

import in.edu.jspmjscoe.admissionportal.model.security.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "head_leaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeadLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long headLeaveId;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(nullable = false)
    private int numberOfDays;

    @Column(nullable = false)
    private String reason;

    private String prefixSuffix; // optional holidays/sunday

    private String substituteTeacherName; // who will look after work

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private Status status = Status.PENDING; // default

    // Relationship with Teacher (who is applying)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
}
