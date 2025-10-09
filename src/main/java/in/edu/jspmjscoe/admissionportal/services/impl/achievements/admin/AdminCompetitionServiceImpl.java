package in.edu.jspmjscoe.admissionportal.services.impl.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionMarksUpdateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.exception.achievement.CompetitionMarkException;
import in.edu.jspmjscoe.admissionportal.model.achievements.Competition;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TrainingPlacementRecord;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.CompetitionRepository;
import in.edu.jspmjscoe.admissionportal.repositories.trainingplacement.TrainingPlacementRecordRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminCompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCompetitionServiceImpl implements AdminCompetitionService {

    private final CompetitionRepository competitionRepository;
    private final TrainingPlacementRecordRepository trainingPlacementRecordRepository;

    private static final double MAX_MARK_PER_COMPETITION = 10.0;

    @Override
    @Transactional
    public CompetitionUpdateResultDTO assignCompetitionMarksBulk(List<CompetitionMarksUpdateDTO> updates) {
        if (updates == null || updates.isEmpty()) {
            return new CompetitionUpdateResultDTO(List.of(), 0.0);
        }

        Map<Long, Double> competitionMarks = updates.stream()
                .collect(Collectors.toMap(
                        CompetitionMarksUpdateDTO::getCompetitionId,
                        CompetitionMarksUpdateDTO::getMarks,
                        (existing, replacement) -> replacement // handle duplicates
                ));

        List<Competition> competitions = competitionRepository.findAllById(competitionMarks.keySet());

        if (competitions.isEmpty()) {
            throw new CompetitionMarkException("No valid competitions found for provided IDs.");
        }

        competitions.forEach(competition -> {
            Double marks = competitionMarks.get(competition.getCompetitionId());
            if (marks == null) return;

            if (marks < 0 || marks > MAX_MARK_PER_COMPETITION) {
                throw new CompetitionMarkException(
                        "Invalid marks (" + marks + ") for competitionId=" + competition.getCompetitionId()
                                + ". Allowed range: 0–" + MAX_MARK_PER_COMPETITION
                );
            }

            competition.setMarks(marks);
        });

        competitionRepository.saveAll(competitions);


        StudentAcademicYear studentAcademicYear = competitions.get(0).getStudentAcademicYear();
        double recalculatedScore = recalcAndSaveCompetitionScore(studentAcademicYear);

        return new CompetitionUpdateResultDTO(
                competitions.stream().map(Competition::getCompetitionId).toList(),
                recalculatedScore
        );
    }

    private double recalcAndSaveCompetitionScore(StudentAcademicYear studentAcademicYear) {
        List<Competition> allCompetitions = competitionRepository.findByStudentAcademicYear(studentAcademicYear);

        // ⚡ Only consider competitions with marks > 0
        List<Competition> gradedCompetitions = allCompetitions.stream()
                .filter(c -> c.getMarks() != null)
                .toList();

        double totalObtained = gradedCompetitions.stream()
                .mapToDouble(Competition::getMarks)
                .sum();

        double totalPossible = gradedCompetitions.size() * MAX_MARK_PER_COMPETITION;
        if (totalPossible <= 0) {
            return 0.0; // nothing graded yet
        }

        double convertedScore = (totalObtained / totalPossible) * 10.0;
        double convertedScoreRounded = Math.round(convertedScore * 100.0) / 100.0;

        TrainingPlacementRecord tpr = trainingPlacementRecordRepository
                .findByStudentAcademicYear(studentAcademicYear)
                .orElseGet(() -> {
                    TrainingPlacementRecord r = new TrainingPlacementRecord();
                    r.setStudentAcademicYear(studentAcademicYear);
                    return r;
                });

        tpr.setNationalEvent(convertedScoreRounded);
        trainingPlacementRecordRepository.save(tpr);

        return convertedScoreRounded;
    }

}
