package in.edu.jspmjscoe.admissionportal.model.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hsc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HSC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hscId;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @JsonBackReference
    private Student student;   // FK → Student.student_id

    @Column(nullable = false)
    private String board;

    @Column(name = "passing_year")
    private Integer passingYear;

    @Column(name = "seat_no", unique = true)
    private String seatNo;

    @Column(name = "physics_percentage")
    private Double physicsPercentage;

    @Column(name = "chemistry_percentage")
    private Double chemistryPercentage;

    @Column(name = "math_percentage")
    private Double mathPercentage;

    @Column(name = "additional_subject_name")
    private String additionalSubjectName;

    @Column(name = "additional_subject_percentage")
    private Double additionalSubjectPercentage;

    @Column(name = "english_percentage")
    private Double englishPercentage;

    @Column(name = "total_percentage")
    private Double totalPercentage;

    @Column(name = "eligibility_percentage")
    private Double eligibilityPercentage;
}
