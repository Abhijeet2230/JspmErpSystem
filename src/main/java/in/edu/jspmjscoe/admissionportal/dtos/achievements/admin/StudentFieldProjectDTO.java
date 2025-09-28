package in.edu.jspmjscoe.admissionportal.dtos.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectViewDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFieldProjectDTO extends BaseStudentAchievementDTO {

    private List<FieldProjectViewDTO> fieldProjects;
}
