package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionMarksUpdateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionUpdateResultDTO;

import java.util.List;
import java.util.Map;

public interface AdminCompetitionService {
    CompetitionUpdateResultDTO assignCompetitionMarksBulk(List<CompetitionMarksUpdateDTO> updates);
}
