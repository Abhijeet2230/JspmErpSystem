package in.edu.jspmjscoe.admissionportal.model.assessment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "student_unit_assessments",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"student_academic_year_id", "subject_id", "unit_number"}
        )
)
public class StudentUnitAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_unit_assessment_id")
    private Long studentUnitAssessmentId;

    // Link to StudentAcademicYear
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_academic_year_id", nullable = false)
    @JsonBackReference
    private StudentAcademicYear studentAcademicYear;

    // Link to Subject
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonBackReference
    private Subject subject;

    @Column(name = "unit_number", nullable = false)
    private Integer unitNumber; // 1–5

    @Column(name = "quiz_marks", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double quizMarks;

    @Column(name = "activity_marks", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double activityMarks;
}
