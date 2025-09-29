package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateUpdateResultDTO;

import java.util.Map;

public interface AdminCertificateService {
    
    /**
     * Bulk assign marks to multiple certificates and update TrainingPlacementRecord accordingly.
     *
     * @param certificateMarks Map of certificateId to marks (out of 10)
     */
    CertificateUpdateResultDTO assignCertificateMarksBulk(Map<Long, Double> certificateMarks);
}
