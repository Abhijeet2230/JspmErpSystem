package in.edu.jspmjscoe.admissionportal.dtos.internship;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndustrialVisitDTO {

    private Long id;
    private String visitTitle;
    private String organization;
    private String venue;
    private LocalDate visitDate;
    private Double durationHours;
    private Boolean certificateProvided;
    private Long referredByUserId;
    private String referredByUsername; // for display purposes
}
