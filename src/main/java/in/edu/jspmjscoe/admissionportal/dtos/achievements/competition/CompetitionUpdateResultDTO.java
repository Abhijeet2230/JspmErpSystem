package in.edu.jspmjscoe.admissionportal.dtos.achievements.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionUpdateResultDTO {
    private List<Long> updatedCompetitionIds;
    private double updatedNationalEventScore; // recalculated score stored in TrainingPlacementRecord
}
