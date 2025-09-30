package in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FieldProjectBulkUpdateRequestDTO {
    private List<FieldProjectUpdateRequestDTO> updates;
}
