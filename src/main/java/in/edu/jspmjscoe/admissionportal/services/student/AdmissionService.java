package in.edu.jspmjscoe.admissionportal.services.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.AdmissionDTO;
import java.util.List;

public interface AdmissionService {
    AdmissionDTO createAdmission(AdmissionDTO admissionDTO, Long studentId);
    AdmissionDTO getAdmissionById(Long id);
    List<AdmissionDTO> getAllAdmissions();
    AdmissionDTO updateAdmission(Long id, AdmissionDTO admissionDTO);
    void deleteAdmission(Long id);
}
