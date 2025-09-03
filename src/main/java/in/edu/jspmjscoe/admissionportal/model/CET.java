package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CET {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cet_id")
    private Long cetId;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @JsonBackReference
    private Student student;   // FK → students.student_id (strict 1:1)

    @Column(name = "roll_no", unique = true)
    private String rollNo;

    @Column(name = "percentile")
    private Double percentile;
}
