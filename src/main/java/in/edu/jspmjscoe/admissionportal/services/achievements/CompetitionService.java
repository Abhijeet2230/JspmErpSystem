package in.edu.jspmjscoe.admissionportal.services.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;

public interface CompetitionService {
    CompetitionDTO saveCompetition(CompetitionDTO competitionDTO, StudentAcademicYear academicYear);
    CompetitionDTO getCompetitionById(Long competitionId);
}
