package in.edu.jspmjscoe.admissionportal.dtos.teacher;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherSubjectDto {
    private Long teacherSubjectId;
    private Long teacherId;
    private Long subjectId;
}
