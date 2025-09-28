package in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiniProjectDTO {

    private Long miniProjectId;          // PK

    private String name;
    private String domain;
    private String outcome;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long studentAcademicYearId;  // only the id instead of full object

    private String videoMinioKey;        // video file key in MinIO
    private String pdfMinioKey;          // pdf file key in MinIO
}
