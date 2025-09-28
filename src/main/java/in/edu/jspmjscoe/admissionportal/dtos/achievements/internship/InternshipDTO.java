package in.edu.jspmjscoe.admissionportal.dtos.achievements.internship;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternshipDTO {

    private Long internshipId;
    private String companyName;
    private String assignmentName;
    private String role;
    private String outcome;
    private LocalDate startDate;
    private LocalDate endDate;
    private String duration;
    private String minioObjectKey;

    private Long studentAcademicYearId;
}
