package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitMarksDTO {
    private Integer unitNumber;
    private Double quizMarks;
    private Double activityMarks;
}