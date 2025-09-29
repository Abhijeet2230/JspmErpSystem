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
        name = "student_exams",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_academic_year_id", "subject_id", "exam_type"})
)
public class StudentExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_exam_id")
    private Long studentExamId;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false)
    private ExamType examType; // MID_TERM / END_TERM

    @Column(name = "marks_obtained", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double marksObtained;
}
