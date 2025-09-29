package in.edu.jspmjscoe.admissionportal.dtos.achievements.competition;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompetitionViewDTO {
    private Long competitionId;
    private String name;
    private String organizer;
    private String level;
    private String award;
    private LocalDate date;
    private Double marks;

    private String fileUrl; // presigned URL
}