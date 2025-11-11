package in.edu.jspmjscoe.admissionportal.model.internship;

import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "industrial_visit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndustrialVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "visit_title", nullable = false)
    private String visitTitle;

    @Column(name = "organization", nullable = false)
    private String organization;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(name = "certificate_provided")
    private Boolean certificateProvided;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_by_user_id")
    private User referredBy;

    // Many students can attend many industrial visits
    @ManyToMany
    @JoinTable(
        name = "industrial_visit_students",
        joinColumns = @JoinColumn(name = "industrial_visit_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
}
