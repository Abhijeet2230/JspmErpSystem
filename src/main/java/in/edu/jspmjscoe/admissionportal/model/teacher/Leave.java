package in.edu.jspmjscoe.admissionportal.model.teacher;

import in.edu.jspmjscoe.admissionportal.model.security.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "leaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    @Column(nullable = false)
    private String day; // e.g. "Monday"

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalTime leaveTime;

    @Column(name = "actual_closer_time")
    private LocalTime actualCloserTime;

    @Column(name = "leave_hours")
    private Double leaveHours; // store hours difference


    @Column(nullable = false)
    private LocalTime closerTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private Status status = Status.PENDING; // default

    // Relationship with Teacher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
}
