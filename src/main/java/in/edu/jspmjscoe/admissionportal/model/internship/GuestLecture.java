package in.edu.jspmjscoe.admissionportal.model.internship;

import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "guest_lecture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestLecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lecture_title", nullable = false)
    private String lectureTitle;

    @Column(name = "speaker_name", nullable = false)
    private String speakerName;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "organization")
    private String organization;

    @Column(name = "lecture_date")
    private LocalDate lectureDate;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(name = "certificate_provided")
    private Boolean certificateProvided;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_by_user_id")
    private User referredBy;

    // Many students can attend many guest lectures
    @ManyToMany
    @JoinTable(
        name = "guest_lecture_students",
        joinColumns = @JoinColumn(name = "guest_lecture_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
}
