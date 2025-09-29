package in.edu.jspmjscoe.admissionportal.services.impl.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipViewDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectViewDTO;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.certificate.CertificateViewMapper;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.competition.CompetitionViewMapper;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.fieldproject.FieldProjectViewMapper;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.internship.InternshipViewMapper;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.miniproject.MiniProjectViewMapper;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.*;
import in.edu.jspmjscoe.admissionportal.services.achievements.AchievementViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementViewServiceImpl implements AchievementViewService {

    private final CertificateRepository certificateRepository;
    private final InternshipRepository internshipRepository;
    private final CompetitionRepository competitionRepository;
    private final MiniProjectRepository miniProjectRepository;
    private final FieldProjectRepository fieldProjectRepository;
    private final CertificateViewMapper certificateViewMapper;
    private final InternshipViewMapper internshipViewMapper;
    private final CompetitionViewMapper competitionViewMapper;
    private final MiniProjectViewMapper miniProjectViewMapper;
    private final FieldProjectViewMapper fieldProjectViewMapper;

    @Override
    public List<CertificateViewDTO> getCertificates(Student student) {
        return certificateRepository.findByStudentAcademicYear_Student(student)
                .stream().map(certificateViewMapper::toDTO).toList();
    }

    @Override
    public List<InternshipViewDTO> getInternships(Student student) {
        return internshipRepository.findByStudentAcademicYear_Student(student)
                .stream().map(internshipViewMapper::toDTO).toList();
    }

    @Override
    public List<CompetitionViewDTO> getCompetitions(Student student) {
        return competitionRepository.findByStudentAcademicYear_Student(student)
                .stream().map(competitionViewMapper::toDTO).toList();
    }

    @Override
    public List<MiniProjectViewDTO> getMiniProjects(Student student) {
        return miniProjectRepository.findByStudentAcademicYear_Student(student)
                .stream().map(miniProjectViewMapper::toDTO).toList();
    }

    @Override
    public List<FieldProjectViewDTO> getFieldProjects(Student student) {
        return fieldProjectRepository.findByStudentAcademicYear_Student(student)
                .stream().map(fieldProjectViewMapper::toDTO).toList();
    }
}
