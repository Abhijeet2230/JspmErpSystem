package in.edu.jspmjscoe.admissionportal.dtos.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipViewDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentInternshipDTO extends BaseStudentAchievementDTO {

    private List<InternshipViewDTO> internships;
}
