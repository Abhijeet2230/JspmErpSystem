package in.edu.jspmjscoe.admissionportal.services.impl.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.certificate.CertificateMapper;
import in.edu.jspmjscoe.admissionportal.model.achievements.Certificate;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.CertificateRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final StudentAcademicYearRepository studentAcademicYearRepository;
    private final CertificateMapper certificateMapper;

    @Override
    @Transactional
    public CertificateDTO saveCertificate(CertificateDTO certificateDTO, StudentAcademicYear studentAcademicYear) {
        // Map DTO to entity
        Certificate certificate = certificateMapper.toEntity(certificateDTO);
        certificate.setStudentAcademicYear(studentAcademicYear);

        // Save to DB
        Certificate saved = certificateRepository.save(certificate);

        // Map back to DTO and return
        return certificateMapper.toDTO(saved);
    }


    @Override
    public CertificateDTO getCertificateById(Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Certificate not found with ID " + certificateId
                ));
        return certificateMapper.toDTO(certificate);
    }
}
