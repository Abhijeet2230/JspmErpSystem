package in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FieldProjectViewDTO {
    private Long fieldProjectId;
    private String name;
    private String domain;
    private String outcome;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double marks;
    private String videoUrl; // presigned URL
    private String pdfUrl;// presigned URL
    // 🔹 New fields
    private Long subjectId;               // linked subject ID
    private String subjectName;           // subject name
    private Integer unitNumber;
}