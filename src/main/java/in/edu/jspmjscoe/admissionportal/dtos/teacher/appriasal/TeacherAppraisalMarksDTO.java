package in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAppraisalMarksDTO {
    private Long appraisalId;
    private Double marks;
}
