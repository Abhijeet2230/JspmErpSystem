package in.edu.jspmjscoe.admissionportal.model.internship;

import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "training_skill_workshop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSkillWorkshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "workshop_title", nullable = false)
    private String workshopTitle;

    @Column(name = "organization")
    private String organization;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "workshop_date")
    private LocalDate workshopDate;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(name = "certificate_provided")
    private Boolean certificateProvided;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_by_user_id")
    private User referredBy;

    // Many students can attend many workshops
    @ManyToMany
    @JoinTable(
        name = "workshop_students",
        joinColumns = @JoinColumn(name = "workshop_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
}
