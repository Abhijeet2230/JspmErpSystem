package in.edu.jspmjscoe.admissionportal.services.impl.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipMarksUpdateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.exception.achievement.InternshipMarkException;
import in.edu.jspmjscoe.admissionportal.model.achievements.Internship;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TrainingPlacementRecord;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.InternshipRepository;
import in.edu.jspmjscoe.admissionportal.repositories.trainingplacement.TrainingPlacementRecordRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminInternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInternshipServiceImpl implements AdminInternshipService {

    private final InternshipRepository internshipRepository;
    private final TrainingPlacementRecordRepository trainingPlacementRecordRepository;

    private static final double MAX_MARK_PER_INTERNSHIP = 10.0;

    @Override
    @Transactional
    public InternshipUpdateResultDTO assignInternshipMarksBulk(List<InternshipMarksUpdateDTO> updates) {
        if (updates == null || updates.isEmpty()) {
            return new InternshipUpdateResultDTO(List.of(), 0.0);
        }

        Map<Long, Double> internshipMarks = updates.stream()
                .collect(Collectors.toMap(
                        InternshipMarksUpdateDTO::getInternshipId,
                        InternshipMarksUpdateDTO::getMarks,
                        (existing, replacement) -> replacement // handle duplicates
                ));

        List<Internship> internships = internshipRepository.findAllById(internshipMarks.keySet());

        if (internships.isEmpty()) {
            throw new InternshipMarkException("No valid internships found for provided IDs.");
        }

        internships.forEach(internship -> {
            Double marks = internshipMarks.get(internship.getInternshipId());
            if (marks == null) return;

            if (marks < 0 || marks > MAX_MARK_PER_INTERNSHIP) {
                throw new InternshipMarkException(
                        "Invalid marks (" + marks + ") for internshipId=" + internship.getInternshipId()
                                + ". Allowed range: 0–" + MAX_MARK_PER_INTERNSHIP
                );
            }

            internship.setMarks(marks);
        });

        internshipRepository.saveAll(internships);

        // recalc total score for student
        StudentAcademicYear studentAcademicYear = internships.get(0).getStudentAcademicYear();
        double recalculatedScore = recalcAndSaveInternshipScore(studentAcademicYear);

        return new InternshipUpdateResultDTO(
                internships.stream().map(Internship::getInternshipId).toList(),
                recalculatedScore
        );
    }


    private double recalcAndSaveInternshipScore(StudentAcademicYear studentAcademicYear) {
        List<Internship> allInternships = internshipRepository.findByStudentAcademicYear(studentAcademicYear);

        // only consider assigned marks
        List<Internship> graded = allInternships.stream()
                .filter(i -> i.getMarks() != null)
                .toList();

        if (graded.isEmpty()) return 0.0;

        double totalObtained = graded.stream().mapToDouble(Internship::getMarks).sum();
        double totalPossible = graded.size() * MAX_MARK_PER_INTERNSHIP;

        double convertedScore = (totalObtained / totalPossible) * 10.0;
        double convertedScoreRounded = Math.round(convertedScore * 100.0) / 100.0;

        TrainingPlacementRecord tpr = trainingPlacementRecordRepository
                .findByStudentAcademicYear(studentAcademicYear)
                .orElseGet(() -> {
                    TrainingPlacementRecord r = new TrainingPlacementRecord();
                    r.setStudentAcademicYear(studentAcademicYear);
                    return r;
                });

        tpr.setInternship(convertedScoreRounded);
        trainingPlacementRecordRepository.save(tpr);

        return convertedScoreRounded;
    }
}
