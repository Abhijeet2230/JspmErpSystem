package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectBulkUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectUpdateRequestDTO;

public interface AdminFieldProjectService {
    void updateFieldProject(FieldProjectUpdateRequestDTO request);
    void updateFieldProjectBulk(FieldProjectBulkUpdateRequestDTO request);
}
