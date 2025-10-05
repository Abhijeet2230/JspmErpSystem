package in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateMarksUpdateDTO {
    private Long certificateId;
    private Double marks;
}
