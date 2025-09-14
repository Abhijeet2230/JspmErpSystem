package in.edu.jspmjscoe.admissionportal.dtos.subject;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long courseId;
    private String name;
    private String code;
    private String courseType;
    private Integer durationYears;
    private Integer totalCredits;
    private Integer intake;

    // Optional: just IDs instead of full objects (to avoid infinite loops)
    private Long departmentId;

    // If you need student info in DTO response
    private List<Long> studentIds;

    // If you need subject info in DTO response
    private List<Long> subjectIds;
}
