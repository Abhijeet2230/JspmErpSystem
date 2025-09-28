package in.edu.jspmjscoe.admissionportal.dtos.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateViewDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCertificateDTO extends BaseStudentAchievementDTO {

    private List<CertificateViewDTO> certificates;
}
