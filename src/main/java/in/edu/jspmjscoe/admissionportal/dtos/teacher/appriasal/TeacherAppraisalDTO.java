package in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAppraisalDTO {

    private Long appraisalId;

    private Long teacherId;
    private String teacherName;     // Resolved from Teacher entity
    private String departmentName;
    private String academicTab;
    private String workContribution;
    private String academicYear;
    private LocalDate activityDate;
    private String activityDescription;

    private String iicActivityType;
    private String eventType;
    private String nbaCriteria;
    private String naacCriteria;

    private String appraisalDocumentPath;
    private String activityPhotoPath;
    private String activityVideoPath;
}
