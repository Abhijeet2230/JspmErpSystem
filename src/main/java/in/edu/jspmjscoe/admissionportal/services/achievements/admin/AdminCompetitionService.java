package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionUpdateResultDTO;

import java.util.Map;

public interface AdminCompetitionService {
    CompetitionUpdateResultDTO assignCompetitionMarksBulk(Map<Long, Double> competitionMarks);
}
