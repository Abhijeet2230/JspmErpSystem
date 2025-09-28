package in.edu.jspmjscoe.admissionportal.services.impl.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.competition.CompetitionMapper;
import in.edu.jspmjscoe.admissionportal.model.achievements.Competition;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.CompetitionRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final StudentAcademicYearRepository studentAcademicYearRepository;
    private final CompetitionMapper competitionMapper;

    @Override
    @Transactional
    public CompetitionDTO saveCompetition(CompetitionDTO competitionDTO, StudentAcademicYear academicYear) {
        Competition competition = competitionMapper.toEntity(competitionDTO);
        competition.setStudentAcademicYear(academicYear);

        Competition saved = competitionRepository.save(competition);
        return competitionMapper.toDTO(saved);
    }


    @Override
    public CompetitionDTO getCompetitionById(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Competition not found with ID " + competitionId));
        return competitionMapper.toDTO(competition);
    }
}
