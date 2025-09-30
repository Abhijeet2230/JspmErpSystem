package in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldProjectSingleUpdateResultDTO {
    private Long fieldProjectId;
    private Long studentAcademicYearId;
    private Long subjectId;
    private Integer unitNumber;
    private Double marks;
}
