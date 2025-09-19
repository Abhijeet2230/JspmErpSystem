package in.edu.jspmjscoe.admissionportal.dtos.trainingplacement;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkTrainingPlacementPatchRequest {

    private List<TrainingPlacementPatchDTO> updates;
}
