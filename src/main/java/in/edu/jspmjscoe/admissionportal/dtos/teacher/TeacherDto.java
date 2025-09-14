package in.edu.jspmjscoe.admissionportal.dtos.teacher;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDto {
    private Long teacherId;
    private String name;
    private String email;
    private String phone;
    private String designation;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships
    private Long departmentId;
    private List<Long> teacherSubjectIds;
}
