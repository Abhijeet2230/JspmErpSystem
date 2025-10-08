package in.edu.jspmjscoe.admissionportal.model.internship;

import in.edu.jspmjscoe.admissionportal.model.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "placement_application",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "placement_id"})
) //To ensure a student cannot apply to the same placement twice
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "placement_id", nullable = false)
    private Placement placement;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "applied_date", nullable = false)
    @Builder.Default
    private LocalDate appliedDate = LocalDate.now();

    @Column(name = "selected_date")
    private LocalDate selectedDate;

    @Column(name = "rejected_date")
    private LocalDate rejectedDate;

    @Column(name = "remarks", length = 1000)
    private String remarks;
    
    // Application-specific fields
    @Column(name = "cover_letter", length = 2000)
    private String coverLetter;
    
    @Column(name = "why_interested", length = 1000)
    private String whyInterested;
    
    @Column(name = "expected_salary", length = 50)
    private String expectedSalary;
    
    @Column(name = "preferred_location", length = 100)
    private String preferredLocation;
}



