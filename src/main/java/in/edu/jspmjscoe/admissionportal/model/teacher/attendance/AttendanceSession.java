package in.edu.jspmjscoe.admissionportal.model.teacher.attendance;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "attendance_session",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "teacher_id", "subject_id", "division", "date", "start_time", "end_time"
                })
        }
)
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "division", nullable = false)
    private String division;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "attendanceSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AttendanceStudent> studentAttendances = new ArrayList<>();

    // Helper to maintain bidirectional link
    public void addAttendanceStudent(AttendanceStudent student) {
        if (studentAttendances == null) studentAttendances = new ArrayList<>();
        studentAttendances.add(student);
        student.setAttendanceSession(this);
    }
}
