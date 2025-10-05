package in.edu.jspmjscoe.admissionportal.services.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.*;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface AchievementsService {

    CertificateDTO uploadCertificate(UserDetails userDetails, CertificateDTO dto, MultipartFile file);

    InternshipDTO uploadInternship(UserDetails userDetails, InternshipDTO dto, MultipartFile file);

    CompetitionDTO uploadCompetition(UserDetails userDetails, CompetitionDTO dto, MultipartFile file);

    MiniProjectDTO uploadMiniProject(UserDetails userDetails, MiniProjectDTO dto, MultipartFile video, MultipartFile pdf);

    FieldProjectDTO uploadFieldProject(UserDetails userDetails, FieldProjectDTO dto, MultipartFile video, MultipartFile pdf);

    MyAchievementsDTO getMyAchievements(UserDetails userDetails);
}
