package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "admission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    @Column(name = "merit_no")
    private Integer meritNo;

    @Column(name = "merit_marks")
    private Double meritMarks;

    @Column(name = "institute_code")
    private String instituteCode;

    @Column(name = "institute_name")
    private String instituteName;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "choice_code")
    private String choiceCode;

    @Column(name = "seat_type")
    private String seatType;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "reported_date")
    private LocalDate reportedDate;

    @Column(name = "is_current")
    private Boolean isCurrent;

    @Enumerated(EnumType.STRING)
    @Column(name = "admission_status")
    private AdmissionStatus admissionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_year")
    private CurrentYear currentYear;

}
