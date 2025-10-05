package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.request.FieldProjectBulkUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.request.FieldProjectUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.response.FieldProjectBulkUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.response.FieldProjectSingleUpdateResultDTO;

public interface AdminFieldProjectService {

    // Single update
    FieldProjectSingleUpdateResultDTO updateFieldProject(FieldProjectUpdateRequestDTO request);

    // Bulk update
    FieldProjectBulkUpdateResultDTO updateFieldProjectsBulk(FieldProjectBulkUpdateRequestDTO bulkRequest);
}
