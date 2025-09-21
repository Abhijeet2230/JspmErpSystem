package in.edu.jspmjscoe.admissionportal.model.attendance;

import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherSubject;
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
@Table(name = "attendance",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"teacher_subject_id", "attendance_date", "session"}
        ))
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_subject_id", nullable = false)
    private TeacherSubject teacherSubject;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    // optional: "Morning", "Afternoon", "Period-1" etc
    @Column(name = "session", length = 50)
    private String session;

    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceEntry> entries = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // helper
    public void addEntry(AttendanceEntry entry) {
        entries.add(entry);
        entry.setAttendance(this);
    }
}
