package in.edu.jspmjscoe.admissionportal.dtos.achievements.internship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternshipMarksUpdateDTO {
    private Long internshipId;
    private Double marks;
}