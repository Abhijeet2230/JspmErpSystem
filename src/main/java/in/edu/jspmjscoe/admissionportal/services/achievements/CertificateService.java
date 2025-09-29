package in.edu.jspmjscoe.admissionportal.services.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;

public interface CertificateService {

    /**
     * Save certificate details into DB.
     *
     * @param certificateDTO DTO containing certificate info and MinIO object key
     * @return saved CertificateDTO
     */
    CertificateDTO saveCertificate(CertificateDTO certificateDTO, StudentAcademicYear studentAcademicYear);

    /**
     * Optional: fetch a certificate by ID
     */
    CertificateDTO getCertificateById(Long certificateId);
}
