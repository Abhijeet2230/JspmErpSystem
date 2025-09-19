package in.edu.jspmjscoe.admissionportal.dtos.trainingplacement;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPlacementDTO {

    private Integer rollNo;
    private String name;
    private String division;

    // Wraps all the T&P record fields and tests
    private TrainingPlacementRecordDTO trainingPlacement;
}
