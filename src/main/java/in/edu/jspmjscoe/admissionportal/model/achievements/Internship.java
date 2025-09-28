package in.edu.jspmjscoe.admissionportal.model.achievements;

import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "internship")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internshipId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    private String role;

    @Column(name = "assignment_name", columnDefinition = "TEXT")
    private String assignmentName;

    @Column(name = "outcome", columnDefinition = "TEXT")
    private String outcome;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "minio_object_key", nullable = false, unique = true)
    private String minioObjectKey; // MinIO path of internship certificate/letter

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_academic_year_id", nullable = false)
    private StudentAcademicYear studentAcademicYear;

    // 🔹 Marks with default value 0.0
    @Column(name = "marks", nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double marks = 0.0;
}
