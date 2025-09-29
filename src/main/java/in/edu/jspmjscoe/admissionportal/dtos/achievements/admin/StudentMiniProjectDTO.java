package in.edu.jspmjscoe.admissionportal.dtos.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectViewDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentMiniProjectDTO extends BaseStudentAchievementDTO {

    private List<MiniProjectViewDTO> miniProjects;
}
