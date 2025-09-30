package in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FieldProjectBulkUpdateResultDTO {
    private List<FieldProjectSingleUpdateResultDTO> updates;
}
