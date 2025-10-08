package in.edu.jspmjscoe.admissionportal.model.teacher.appriasal;

import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "teacher_appraisal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAppraisal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appraisalId;

    // Relation with Teacher (foreign key)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(nullable = false)
    private String departmentName;

    @Column(nullable = false)
    private String workContribution;  // Dropdown selection (mapped to 9 tabs)

    @Column(nullable = false)
    private String academicYear;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Column(length = 1000)
    private String activityDescription;

    @Column(nullable = false)
    private String academicTab;

    @Column(nullable = false)
    private String iicActivityType;   // Level 1 / Level 2 / Level 3 / Level 4

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String nbaCriteria;       // Multiple selection allowed in frontend, comma-separated

    @Column(nullable = false)
    private String naacCriteria;      // Multiple selection allowed in frontend, comma-separated

    // ----------------- Admin Marks -----------------
    @Column(nullable = true)
    private Double marks;  // Nullable initially; admin will set later

    @Column(nullable = false)
    private String appraisalDocumentPath;

    @Column(nullable = false)
    private String activityPhotoPath;

    private String activityVideoPath;

}
