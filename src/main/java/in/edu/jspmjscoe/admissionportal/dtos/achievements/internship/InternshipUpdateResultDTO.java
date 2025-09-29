package in.edu.jspmjscoe.admissionportal.dtos.achievements.internship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipUpdateResultDTO {
    private List<Long> updatedInternshipIds;
    private double recalculatedScore;
}
