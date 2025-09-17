package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.Data;

@Data
public class UnitUpdateRequestDTO {
    private Long unitId;
    private Double quizMarks;
    private Double activityMarks;
}
