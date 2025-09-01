package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ssc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ssc_id")
    private Long sscId;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @JsonBackReference
    private Student student;   // FK → Student.student_id (strict 1:1)

    @Column(name = "board", nullable = false)
    private String board;

    @Column(name = "passing_year", nullable = false)
    private String passingYear;

    @Column(name = "seat_no", nullable = false, unique = true)
    private String seatNo;

    @Column(name = "math_percentage")
    private Double mathPercentage;

    @Column(name = "total_percentage")
    private Double totalPercentage;
}
