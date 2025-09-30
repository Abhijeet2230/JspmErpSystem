package in.edu.jspmjscoe.admissionportal.services.impl.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.exception.achievement.CertificateMarkException;
import in.edu.jspmjscoe.admissionportal.model.achievements.Certificate;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TrainingPlacementRecord;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.CertificateRepository;
import in.edu.jspmjscoe.admissionportal.repositories.trainingplacement.TrainingPlacementRecordRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminCertificateServiceImpl implements AdminCertificateService {

    private final CertificateRepository certificateRepository;
    private final TrainingPlacementRecordRepository trainingPlacementRecordRepository;

    private static final double MAX_MARK_PER_CERTIFICATE = 10.0;

    @Override
    @Transactional
    public CertificateUpdateResultDTO assignCertificateMarksBulk(Map<Long, Double> certificateMarks) {
        // 1️⃣ Fetch all certificates by IDs
        List<Certificate> certificates = certificateRepository.findAllById(certificateMarks.keySet());

        // 2️⃣ Update each certificate's marks
        certificates.forEach(cert -> {
            Double marks = certificateMarks.get(cert.getCertificateId());
            if (marks == null) return;

            if (marks < 0 || marks > MAX_MARK_PER_CERTIFICATE) {
                throw new CertificateMarkException(
                        "Invalid marks (" + marks + ") for certificateId=" + cert.getCertificateId()
                                + ". Allowed range: 0–" + MAX_MARK_PER_CERTIFICATE
                );
            }

            cert.setMarks(marks);
        });

        certificateRepository.saveAll(certificates);

        // pick the first student (all belong to the same one in bulk)
        StudentAcademicYear studentAcademicYear = certificates.get(0).getStudentAcademicYear();
        double recalculatedScore = recalcAndSaveCertificateScore(studentAcademicYear);

        // return DTO with IDs + score
        return new CertificateUpdateResultDTO(
                certificates.stream().map(Certificate::getCertificateId).toList(),
                recalculatedScore
        );
    }

    private double recalcAndSaveCertificateScore(StudentAcademicYear studentAcademicYear) {
        List<Certificate> allCertificates = certificateRepository.findByStudentAcademicYear(studentAcademicYear);

        // only consider certificates with assigned marks > 0
        List<Certificate> gradedCertificates = allCertificates.stream()
                .filter(c -> c.getMarks() != null && c.getMarks() > 0.0)
                .toList();

        if (gradedCertificates.isEmpty()) {
            return 0.0; // no marks assigned yet
        }

        double totalObtained = gradedCertificates.stream()
                .mapToDouble(Certificate::getMarks)
                .sum();

        double totalPossible = gradedCertificates.size() * MAX_MARK_PER_CERTIFICATE;

        double convertedScore = (totalObtained / totalPossible) * 10.0;
        double convertedScoreRounded = Math.round(convertedScore * 100.0) / 100.0;

        TrainingPlacementRecord tpr = trainingPlacementRecordRepository
                .findByStudentAcademicYear(studentAcademicYear)
                .orElseGet(() -> {
                    TrainingPlacementRecord r = new TrainingPlacementRecord();
                    r.setStudentAcademicYear(studentAcademicYear);
                    return r;
                });

        tpr.setCertificationCourses(convertedScoreRounded);
        trainingPlacementRecordRepository.save(tpr);

        return convertedScoreRounded;
    }


}
