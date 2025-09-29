package in.edu.jspmjscoe.admissionportal.dtos.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionViewDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCompetitionDTO extends BaseStudentAchievementDTO {

    private List<CompetitionViewDTO> competitions;
}
