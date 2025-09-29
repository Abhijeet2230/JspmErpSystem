package in.edu.jspmjscoe.admissionportal.services.impl.achievements.admin;


import in.edu.jspmjscoe.admissionportal.assemblers.achievements.admin.AdminAchievementAssembler;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.admin.AdminAchievementResponseDTO;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.certificate.CertificateViewMapper;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.competition.CompetitionViewMapper;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.fieldproject.FieldProjectViewMapper;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.internship.InternshipViewMapper;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.miniproject.MiniProjectViewMapper;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.*;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.specifications.student.StudentAcademicYearSpecification;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAchievementServiceImpl implements AdminAchievementService {

    private final CertificateRepository certificateRepository;
    private final InternshipRepository internshipRepository;
    private final CompetitionRepository competitionRepository;
    private final MiniProjectRepository miniProjectRepository;
    private final FieldProjectRepository fieldProjectRepository;
    private final StudentAcademicYearRepository studentAcademicYearRepository;

    private final CertificateViewMapper certificateViewMapper;
    private final InternshipViewMapper internshipViewMapper;
    private final CompetitionViewMapper competitionViewMapper;
    private final MiniProjectViewMapper miniProjectViewMapper;
    private final FieldProjectViewMapper fieldProjectViewMapper;

    private final AdminAchievementAssembler assembler;

    @Override
    public AdminAchievementResponseDTO<?> getAchievements(
            String achievementType,
            Integer yearOfStudy,
            Integer semester,
            String department,
            String course,
            String division,
            Long subjectId
    ) {

        // 1️⃣ Fetch students using optional filters
        List<StudentAcademicYear> studentAcademicYears = studentAcademicYearRepository.findAll(
                StudentAcademicYearSpecification.filterBy(
                        yearOfStudy,
                        semester,
                        department,
                        course,
                        division
                )
        );

        if (studentAcademicYears.isEmpty()) {
            return new AdminAchievementResponseDTO<>(achievementType, Collections.emptyList());
        }

        // 2️⃣ Switch by achievement type
        switch (achievementType) {

            case "Certificate":
                return assembler.assemble(
                        "Certificate",
                        studentAcademicYears,
                        studentAcademicYear -> certificateRepository.findByStudentAcademicYear(studentAcademicYear)
                                .stream()
                                .map(certificateViewMapper::toDTO)
                                .toList()
                );

            case "Internship":
                return assembler.assemble(
                        "Internship",
                        studentAcademicYears,
                        studentAcademicYear -> internshipRepository.findByStudentAcademicYear(studentAcademicYear)
                                .stream()
                                .map(internshipViewMapper::toDTO)
                                .toList()
                );

            case "Competition":
                return assembler.assemble(
                        "Competition",
                        studentAcademicYears,
                        studentAcademicYear -> competitionRepository.findByStudentAcademicYear(studentAcademicYear)
                                .stream()
                                .map(competitionViewMapper::toDTO)
                                .toList()
                );

            case "MiniProject":
                return assembler.assemble(
                        "MiniProject",
                        studentAcademicYears,
                        studentAcademicYear -> miniProjectRepository.findByStudentAcademicYear(studentAcademicYear)
                                .stream()
                                .map(miniProjectViewMapper::toDTO)
                                .toList()
                );

            case "FieldProject":
                return assembler.assemble(
                        "FieldProject",
                        studentAcademicYears,
                        studentAcademicYear -> {
                            if (subjectId != null) {
                                return fieldProjectRepository.findByStudentAcademicYearAndSubject_SubjectId(studentAcademicYear, subjectId)
                                        .stream()
                                        .map(fieldProjectViewMapper::toDTO)
                                        .toList();
                            } else {
                                return fieldProjectRepository.findByStudentAcademicYear(studentAcademicYear)
                                        .stream()
                                        .map(fieldProjectViewMapper::toDTO)
                                        .toList();
                            }
                        }
                );

            default:
                throw new IllegalArgumentException("Invalid achievement type: " + achievementType);
        }
    }
}

