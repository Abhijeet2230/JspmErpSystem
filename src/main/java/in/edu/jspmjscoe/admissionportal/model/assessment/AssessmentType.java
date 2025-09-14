package in.edu.jspmjscoe.admissionportal.model.assessment;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "assessment_type")
public class AssessmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_type_id")
    private Long assessmentTypeId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;  // e.g., "Semester Exam", "Mid Exam"

    @Column(name = "category", nullable = false, length = 20)
    private String category;  // Exam, Quiz, Activity

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relationships
    @OneToMany(mappedBy = "assessmentType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectAssessment> subjectAssessments = new ArrayList<>();
}
