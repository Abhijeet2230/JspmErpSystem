package in.edu.jspmjscoe.admissionportal.dtos.achievements.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionMarksBulkUpdateRequestDTO {
    private List<CompetitionMarksUpdateDTO> updates;
}