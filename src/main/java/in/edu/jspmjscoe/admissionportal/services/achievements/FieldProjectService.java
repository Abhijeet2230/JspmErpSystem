package in.edu.jspmjscoe.admissionportal.services.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;

public interface FieldProjectService {
    FieldProjectDTO saveFieldProject(FieldProjectDTO fieldProjectDTO, StudentAcademicYear academicYear);
    FieldProjectDTO getFieldProjectById(Long fieldProjectId);
}
