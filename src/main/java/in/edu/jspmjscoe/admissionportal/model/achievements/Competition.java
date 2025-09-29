package in.edu.jspmjscoe.admissionportal.model.achievements;

import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "competition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long competitionId;

    @Column(name = "name")
    private String name;

    @Column(name = "organizer")
    private String organizer;

    @Column(name = "level")
    private String level; // e.g., LOCAL, STATE, NATIONAL, INTERNATIONAL

    @Column(name = "award")
    private String award;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "minio_object_key", nullable = false, unique = true)
    private String minioObjectKey;

    // 🔹 Marks with default value 0.0
    @Column(name = "marks", nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double marks = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_academic_year_id", nullable = false)
    private StudentAcademicYear studentAcademicYear;
}
