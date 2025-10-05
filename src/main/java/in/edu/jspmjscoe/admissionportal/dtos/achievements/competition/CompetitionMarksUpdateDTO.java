package in.edu.jspmjscoe.admissionportal.dtos.achievements.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionMarksUpdateDTO {
    private Long competitionId;
    private Double marks;
}