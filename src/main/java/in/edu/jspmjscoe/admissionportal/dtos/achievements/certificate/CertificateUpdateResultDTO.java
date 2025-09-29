package in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateUpdateResultDTO {
    private List<Long> updatedCertificateIds;
    private Double recalculatedScore; // TrainingPlacementRecord score after update
}
