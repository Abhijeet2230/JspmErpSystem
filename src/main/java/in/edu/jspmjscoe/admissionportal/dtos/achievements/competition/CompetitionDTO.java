package in.edu.jspmjscoe.admissionportal.dtos.achievements.competition;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionDTO {
    private Long competitionId;
    private String name;
    private String organizer;
    private String level; // LOCAL, STATE, NATIONAL, INTERNATIONAL
    private String award;
    private LocalDate date;
    private String minioObjectKey; // MinIO path

    private Long studentAcademicYearId;
}