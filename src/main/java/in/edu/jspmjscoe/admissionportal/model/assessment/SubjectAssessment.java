package in.edu.jspmjscoe.admissionportal.model.assessment;

import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subject_assessment")
public class SubjectAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_assessment_id")
    private Long subjectAssessmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_type_id", nullable = false)
    private AssessmentType assessmentType;

    @Column(name = "unit_no")
    private Integer unitNo;  // 1..5 (optional, NULL if not unit-based)

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks;

    @Column(name = "weightage", precision = 5, scale = 2)
    private BigDecimal weightage;

    @Column(name = "assessment_date")
    private LocalDate assessmentDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Relationships
    @OneToMany(mappedBy = "subjectAssessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAssessmentMarks> studentMarks = new ArrayList<>();
}
