package in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldProjectBulkUpdateRequestDTO {
    private Long studentAcademicYearId;
    private Long subjectId;
    private Map<Integer, Double> unitMarks; // unitNumber -> marks
}
