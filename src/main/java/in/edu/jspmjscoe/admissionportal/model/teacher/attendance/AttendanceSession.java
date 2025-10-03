package in.edu.jspmjscoe.admissionportal.model.teacher.attendance;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "attendance_session")
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "division", nullable = false)
    private String division;  // A, B, C

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "period_number", nullable = false)
    private Integer periodNumber;

    @OneToMany(mappedBy = "attendanceSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AttendanceStudent> studentAttendances = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "semester")
    private Integer semester;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void addAttendanceStudent(AttendanceStudent student) {
        if (this.studentAttendances == null) {
            this.studentAttendances = new ArrayList<>();
        }
        student.setAttendanceSession(this); // set the relation
        this.studentAttendances.add(student);
    }

}
