package in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldProjectUpdateRequestDTO {
    private Long fieldProjectId;         // FieldProject row ID
    private Long studentAcademicYearId;  // Student link
    private Long subjectId;              // Subject link
    private Integer unitNumber;          // Unit number (1..5)
    private Double marks;                // Marks out of 10
}
