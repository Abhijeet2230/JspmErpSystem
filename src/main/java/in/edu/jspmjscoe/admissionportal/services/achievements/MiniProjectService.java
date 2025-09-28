package in.edu.jspmjscoe.admissionportal.services.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;

public interface MiniProjectService {
    MiniProjectDTO saveMiniProject(MiniProjectDTO miniProjectDTO, StudentAcademicYear academicYear);
    MiniProjectDTO getMiniProjectById(Long miniProjectId);
}
