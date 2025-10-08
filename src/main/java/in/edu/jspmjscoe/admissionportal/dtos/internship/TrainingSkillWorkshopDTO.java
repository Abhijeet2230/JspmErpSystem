package in.edu.jspmjscoe.admissionportal.dtos.internship;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSkillWorkshopDTO {

    private Long id;
    private String workshopTitle;
    private String organization;
    private String venue;
    private LocalDate workshopDate;
    private Double durationHours;
    private Boolean certificateProvided;
}
