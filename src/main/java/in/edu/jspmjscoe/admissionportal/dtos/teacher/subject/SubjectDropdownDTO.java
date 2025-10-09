package in.edu.jspmjscoe.admissionportal.dtos.teacher.subject;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDropdownDTO {
    private Long subjectId;
    private String subjectName;
}