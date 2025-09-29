package in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class CertificateViewDTO {
    private Long certificateId;
    private String courseName;
    private String organization;
    private LocalDate issueDate;
    private String duration;
    private String grade;
    private Double marks;

    private String fileUrl;  // only this instead of objectKey
}
