package in.edu.jspmjscoe.admissionportal.dtos.student;

import in.edu.jspmjscoe.admissionportal.model.student.AdmissionStatus;
import in.edu.jspmjscoe.admissionportal.model.student.CurrentYear;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionDTO {

    private Long admissionId;
    private Long studentId;        // reference instead of full Student entity
    private Integer meritNo;
    private Double meritMarks;
    private String instituteCode;
    private String instituteName;
    private String courseName;
    private String choiceCode;
    private String seatType;
    private LocalDate admissionDate;
    private LocalDate reportedDate;
    private Boolean isCurrent;
    private AdmissionStatus admissionStatus;
    private CurrentYear currentYear;
}
