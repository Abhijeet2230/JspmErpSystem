package in.edu.jspmjscoe.admissionportal.dtos.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectViewDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyAchievementsDTO {

    private List<CertificateViewDTO> certificates;
    private List<InternshipViewDTO> internships;
    private List<CompetitionViewDTO> competitions;
    private List<MiniProjectViewDTO> miniProjects;
    private List<FieldProjectViewDTO> fieldProjects;
}
