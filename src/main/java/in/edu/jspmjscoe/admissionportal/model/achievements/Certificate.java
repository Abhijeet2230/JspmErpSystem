package in.edu.jspmjscoe.admissionportal.model.achievements;

import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "certificate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "organization")
    private String organization;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    private String duration;

    private String grade;

    /**
     * MinIO object key (path inside the bucket)
     * Example: "certificates/student123/cert1.pdf"
     */
    @Column(name = "minio_object_key", nullable = false, unique = true)
    private String minioObjectKey;

    // 🔹 Marks with default value 0.0
    @Column(name = "marks", nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double marks = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_academic_year_id", nullable = false)
    private StudentAcademicYear studentAcademicYear;
}
