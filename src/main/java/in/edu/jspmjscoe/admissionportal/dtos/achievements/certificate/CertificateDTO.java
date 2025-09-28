package in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDTO {

    private Long certificateId;
    private String courseName;
    private String organization;
    private LocalDate issueDate;
    private String duration;
    private String grade;
    private String minioObjectKey;

    private Long studentAcademicYearId; // link to StudentAcademicYear
}
