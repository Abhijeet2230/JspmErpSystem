package in.edu.jspmjscoe.admissionportal.dtos.teacher.subject;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDropdownDTO {
    private Long teacherId;
    private String teacherName;
}