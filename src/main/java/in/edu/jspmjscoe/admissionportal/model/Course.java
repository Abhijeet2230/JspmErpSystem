package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "course_id")
    private Long courseId;

    // Foreign key to department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Department department;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "course_type", nullable = false, length = 20)
    private String courseType; // e.g., UG, PG, Diploma

    @Column(name = "duration_years", nullable = false)
    private Integer durationYears; // e.g., 4 for UG, 2 for PG

    @Column(name = "total_credits")
    private Integer totalCredits;

    @Column(name = "intake")
    private Integer intake = 0; // default 0

    // One course can have many students
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY)
    @JsonManagedReference // forward side
    @ToString.Exclude
    private List<Student> students = new ArrayList<>();

    // Inside Course.java
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private List<Subject> subjects = new ArrayList<>();

    public void addSubject(Subject subject) {
        subjects.add(subject);
        subject.setCourse(this);
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setCourse(this);
    }
}
