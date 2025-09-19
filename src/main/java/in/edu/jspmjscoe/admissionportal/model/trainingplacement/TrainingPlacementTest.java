package in.edu.jspmjscoe.admissionportal.model.trainingplacement;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training_placement_test")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingPlacementTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to parent record
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tp_record_id", nullable = false)
    private TrainingPlacementRecord trainingPlacementRecord;

    // Type of test (SOFTSKILL / APTITUDE / FUTURE CATEGORIES)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestCategory category;

    // Flexible test name (e.g. "Test1", "Round1")
    @Column(nullable = false)
    private String testName;

    // Score (in Double)
    private Double score;
}
