package in.edu.jspmjscoe.admissionportal.dtos.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultancyProjectApplicationDTO {

    private Long applicationId;
    private Long studentId;
    private String studentName; // for display purposes
    private Long projectId;
    private String projectTitle; // for display purposes
    private String organization; // for display purposes
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private LocalDate selectedDate;
    private LocalDate rejectedDate;
    private String remarks;
}
