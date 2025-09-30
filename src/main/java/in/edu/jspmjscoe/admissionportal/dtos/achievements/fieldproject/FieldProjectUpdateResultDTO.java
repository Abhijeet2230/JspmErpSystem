package in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class FieldProjectUpdateResultDTO {
    private List<Long> updatedFieldProjectIds;
    private Map<Long, Double> syncedCceUpdates; // subject+unit mapping if needed
}
