package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateMarksUpdateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateUpdateResultDTO;

import java.util.List;
import java.util.Map;

public interface AdminCertificateService {
    

    CertificateUpdateResultDTO assignCertificateMarksBulk(List<CertificateMarksUpdateDTO> updates);

}
