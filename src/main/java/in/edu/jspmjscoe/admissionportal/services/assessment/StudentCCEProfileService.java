package in.edu.jspmjscoe.admissionportal.services.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.StudentCCEProfileResponseDTO;

public interface StudentCCEProfileService {

    StudentCCEProfileResponseDTO getStudentCCEProfile(Long studentId);
}
