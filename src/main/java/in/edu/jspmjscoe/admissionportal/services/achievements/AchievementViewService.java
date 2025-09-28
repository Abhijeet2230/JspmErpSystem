package in.edu.jspmjscoe.admissionportal.services.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.*;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectViewDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Student;

import java.util.List;

public interface AchievementViewService {

    List<CertificateViewDTO> getCertificates(Student student);
    List<InternshipViewDTO> getInternships(Student student);
    List<CompetitionViewDTO> getCompetitions(Student student);
    List<MiniProjectViewDTO> getMiniProjects(Student student);
    List<FieldProjectViewDTO> getFieldProjects(Student student);
}
