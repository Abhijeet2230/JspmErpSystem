package in.edu.jspmjscoe.admissionportal.dtos.teacher.subject;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignSubjectRequestDTO {
    private Long teacherId;
    private Long subjectId;
    private List<String> divisions; // ✅ Multiple Divisions Here
}
