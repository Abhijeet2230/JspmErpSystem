package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JEE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jeeId;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @JsonBackReference
    private Student student;   // FK → Student.student_id

    @Column(name = "application_no", nullable = false, unique = true)
    private String applicationNo;

    @Column(nullable = false)
    private Double percentile;
}
