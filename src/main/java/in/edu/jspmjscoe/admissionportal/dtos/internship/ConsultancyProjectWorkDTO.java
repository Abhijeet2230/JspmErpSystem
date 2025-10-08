package in.edu.jspmjscoe.admissionportal.dtos.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.ProjectStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultancyProjectWorkDTO {

    private Long id;
    private String projectTitle;
    private String organization;
    private String location;
    private String projectDescription;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean certificateProvided;
    private ProjectStatus status;
}
