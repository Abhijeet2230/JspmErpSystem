package in.edu.jspmjscoe.admissionportal.dtos.trainingplacement;

import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TestCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingPlacementTestDTO {

    private Long id;
    private TestCategory category;
    private String testName;
    private Double score;
}
