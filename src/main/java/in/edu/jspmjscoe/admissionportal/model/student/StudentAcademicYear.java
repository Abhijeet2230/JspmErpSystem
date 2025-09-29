package in.edu.jspmjscoe.admissionportal.model.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import in.edu.jspmjscoe.admissionportal.model.achievements.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_academic_years",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"student_id", "year_of_study", "semester"}
       ))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_academic_year_id")
    private Long studentAcademicYearId;

    // Permanent Student link
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    @Column(name = "year_of_study", nullable = false)
    private Integer yearOfStudy;   // 1 = FE, 2 = SE, etc.

    @Column(name = "semester", nullable = false)
    private Integer semester;      // 1..8

    @Column(name = "batch_year")
    private String batchYear;     // Admission batch or academic year (e.g., 2023)

    @Column(name = "division")
    private String division;       // A, B, C, etc.

    @Column(name = "roll_no")
    private Integer rollNo;        // Roll number in division

    @Column(name = "semester_start_date")
    private LocalDate semesterStartDate;

    @Column(name = "semester_end_date")
    private LocalDate semesterEndDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "studentAcademicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "studentAcademicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Internship> internships = new ArrayList<>();

    @OneToMany(mappedBy = "studentAcademicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MiniProject> miniProjects = new ArrayList<>();

    @OneToMany(mappedBy = "studentAcademicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldProject> fieldProjects = new ArrayList<>();

    @OneToMany(mappedBy = "studentAcademicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Competition> competitions = new ArrayList<>();

}
