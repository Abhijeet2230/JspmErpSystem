package in.edu.jspmjscoe.admissionportal.dtos.subject;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDTO {
    private Long subjectId;
    private String name;
    private String code;

    private Integer theoryCredits;
    private Integer practicalCredits;
    private Integer yearOfStudy;
    private Integer semester;
    private String subjectType;     // Theory, Lab, Project
    private String subjectCategory; // Core or Elective
    private Integer totalUnits;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships (only IDs for simplicity)
    private Long courseId;
    private Long electiveGroupId;
    private List<Long> teacherSubjectIds;
}
