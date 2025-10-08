package in.edu.jspmjscoe.admissionportal.services.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;

public interface InternshipService {
    
    InternshipDTO saveInternship(InternshipDTO internshipDTO, StudentAcademicYear academicYear);
    
    InternshipDTO getInternshipById(Long internshipId);
}