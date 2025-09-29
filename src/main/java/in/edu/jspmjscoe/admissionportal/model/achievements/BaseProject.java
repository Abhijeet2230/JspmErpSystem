package in.edu.jspmjscoe.admissionportal.model.achievements;

import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseProject {

    @Column(name = "name")
    private String name;

    @Column(name = "domain")
    private String domain;

    @Column(name = "outcome", columnDefinition = "TEXT")
    private String outcome;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_academic_year_id", nullable = false)
    private StudentAcademicYear studentAcademicYear;

    @Column(name = "video_minio_key", nullable = false, unique = true)
    private String videoMinioKey;

    @Column(name = "pdf_minio_key", nullable = false, unique = true)
    private String pdfMinioKey;

    // 🔹 Marks with default value 0.0
    @Column(name = "marks", nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double marks = 0.0;
}
