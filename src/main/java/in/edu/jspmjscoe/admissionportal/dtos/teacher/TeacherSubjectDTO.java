package in.edu.jspmjscoe.admissionportal.dtos.teacher;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherSubjectDTO {
    private Long teacherSubjectId;
    private Long teacherId;
    private String teacherName;
    private Long subjectId;
    private String subjectName;
}


