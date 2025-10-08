package in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStudentSubjectAttendanceDTO {
    private Long studentId;
    private String studentName;
    private String rollNo;
    private String division;

    // subjectName -> percentage (0.0 - 100.0)
    // Use LinkedHashMap to preserve subject ordering
    private Map<String, Double> subjectPercentages = new LinkedHashMap<>();

    // average across subjects (simple mean)
    private Double average;
}
