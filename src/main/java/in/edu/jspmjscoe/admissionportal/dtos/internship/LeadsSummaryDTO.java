package in.edu.jspmjscoe.admissionportal.dtos.internship;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadsSummaryDTO {

    private Long teacherId;
    private String teacherName;
    private Integer totalLeads;
    private Integer internships;
    private Integer placements;
    private Integer consultancyProjects;
    private Integer guestLectures;
    private Integer industrialVisits;
    private Integer trainingWorkshops;
}
