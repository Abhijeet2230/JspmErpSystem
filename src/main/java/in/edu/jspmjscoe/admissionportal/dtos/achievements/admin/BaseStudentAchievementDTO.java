package in.edu.jspmjscoe.admissionportal.dtos.achievements.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseStudentAchievementDTO {

    private Long studentAcademicYearId;
    private String candidateName;
    private Integer rollNo;
    private String division;
    private String courseName;
    private Integer yearOfStudy;
    private Integer semester;
}
