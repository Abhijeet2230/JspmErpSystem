package in.edu.jspmjscoe.admissionportal.model;

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
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // identity column
    @EqualsAndHashCode.Include
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    // One department has many courses
    @OneToMany(mappedBy = "department",
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY)
    @JsonManagedReference // forward side
    @ToString.Exclude
    private List<Course> courses = new ArrayList<>();


    @OneToMany(mappedBy = "department",
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Teacher> teachers = new ArrayList<>();

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacher.setDepartment(this);
    }



    public void addCourse(Course course) {
        courses.add(course);
        course.setDepartment(this);
    }
}
