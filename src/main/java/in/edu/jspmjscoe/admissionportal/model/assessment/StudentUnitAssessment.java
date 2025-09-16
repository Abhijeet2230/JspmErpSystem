package in.edu.jspmjscoe.admissionportal.model.assessment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
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
        columnNames = {"student_id", "subject_id", "unit_number"}
    )
)
public class StudentUnitAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to Student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    // Link to Subject
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonBackReference
    private Subject subject;

    @Column(name = "unit_number", nullable = false)
    private Integer unitNumber; // 1–5

    @Column(name = "quiz_marks")
    private Double quizMarks;

    @Column(name = "activity_marks")
    private Double activityMarks;
}
