package in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherBasicDTO {
    private Long teacherId;
    private String fullName;       // computed as firstName + lastName
    private String designation;
    private Long departmentId;
    private String departmentName;
}
