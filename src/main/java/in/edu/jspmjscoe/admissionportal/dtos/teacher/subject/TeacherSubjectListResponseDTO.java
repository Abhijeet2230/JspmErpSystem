package in.edu.jspmjscoe.admissionportal.dtos.teacher.subject;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherSubjectListResponseDTO {

    private List<TeacherDropdownDTO> teachers;
    private List<SubjectDropdownDTO> subjects;

}
