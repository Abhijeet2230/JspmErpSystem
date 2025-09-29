package in.edu.jspmjscoe.admissionportal.model.achievements;

import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "field_project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldProject extends BaseProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_project_id")
    private Long fieldProjectId;

    // 🔹 Link to Subject
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // 🔹 Unit Number (1–5)
    @Column(name = "unit_number", nullable = false)
    private Integer unitNumber;

}
