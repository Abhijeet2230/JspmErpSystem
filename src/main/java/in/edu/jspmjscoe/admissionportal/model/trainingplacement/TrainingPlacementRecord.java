package in.edu.jspmjscoe.admissionportal.model.trainingplacement;

import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "training_placement_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingPlacementRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // StudentAcademicYear link
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_academic_year_id", nullable = false)
    private StudentAcademicYear studentAcademicYear;

    // Merit (20%)
    private Double sgpaScore; // 0–20

    // Employability (overall non-test fields)
    private Double softskillAttendance;   // 0–10
    private Double certificationCourses;  // 0–10

    // Technical Competency (50%)
    private Double allSubjectQuiz; // 0–10
    private Double internship;     // 0–10
    private Double courseProject;  // 0–10
    private Double nationalEvent;  // 0–10

    // Final totals
    private Double totalScore; // 0–100

    // Link to tests (flexible structure for aptitude/softskills)
    @OneToMany(mappedBy = "trainingPlacementRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingPlacementTest> tests = new ArrayList<>();
}
