package in.edu.jspmjscoe.admissionportal.dtos.achievements.internship;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InternshipViewDTO {
    private Long internshipId;
    private String companyName;
    private String assignmentName;
    private String role;
    private String outcome;
    private LocalDate startDate;
    private LocalDate endDate;
    private String duration;
    private Double marks;


    private String fileUrl; // presigned URL
}