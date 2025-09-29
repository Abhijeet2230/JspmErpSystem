package in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldProjectUpdateRequestDTO {
    private Long studentAcademicYearId;
    private Long subjectId;
    private Integer unitNumber;   // 1–5
    private Double marks;         // out of 10
}
