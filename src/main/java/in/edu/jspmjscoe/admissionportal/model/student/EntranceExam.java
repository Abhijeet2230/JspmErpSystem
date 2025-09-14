package in.edu.jspmjscoe.admissionportal.model.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "entrance_exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntranceExam {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entrance_exam_id")   // 👈 your preferred column name
    private Long entranceExamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    @Column(name = "exam_type", nullable = false)
    private String examType;

    @Column(name = "exam_no", unique = true)
    private String examNo;

    @Column(name = "percentile")
    private Double percentile;
}
