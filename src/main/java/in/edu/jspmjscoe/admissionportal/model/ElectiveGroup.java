package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "elective_group")
public class ElectiveGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;  // e.g., "Advanced AI"

    @Column(name = "max_pick", nullable = false)
    private Integer maxPick = 1;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // One group has many subjects
    @OneToMany(mappedBy = "electiveGroup",
               cascade = CascadeType.ALL,
               orphanRemoval = false)
    @JsonManagedReference
    private List<Subject> subjects = new ArrayList<>();

    public void addSubject(Subject subject) {
        subjects.add(subject);
        subject.setElectiveGroup(this);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
