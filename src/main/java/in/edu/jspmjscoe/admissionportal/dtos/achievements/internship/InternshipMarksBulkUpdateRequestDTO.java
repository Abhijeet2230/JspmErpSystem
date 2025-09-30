package in.edu.jspmjscoe.admissionportal.dtos.achievements.internship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternshipMarksBulkUpdateRequestDTO {
    private List<InternshipMarksUpdateDTO> updates;
}