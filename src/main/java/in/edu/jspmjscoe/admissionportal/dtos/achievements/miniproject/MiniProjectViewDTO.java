package in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MiniProjectViewDTO {
    private Long miniProjectId;
    private String name;
    private String domain;
    private String outcome;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double marks;
    private String videoUrl; // presigned URL
    private String pdfUrl;   // presigned URL
}