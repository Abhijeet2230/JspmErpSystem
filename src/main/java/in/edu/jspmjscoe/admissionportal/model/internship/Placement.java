package in.edu.jspmjscoe.admissionportal.model.internship;

import in.edu.jspmjscoe.admissionportal.model.student.Student;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "placement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Placement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placement_id")
    private Long placementId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "ctc", precision = 12, scale = 2)
    private BigDecimal ctc;

    @Column(name = "bond_required")
    private String bondRequired;

    @Column(name = "vacancies")
    private Integer vacancies;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PostingStatus status = PostingStatus.DRAFT;

    // Many students can apply for a placement
    @ManyToMany
    @JoinTable(name = "placement_students", joinColumns = @JoinColumn(name = "placement_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

    // Bidirectional relationship to applications
    @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlacementApplication> applications;
}
