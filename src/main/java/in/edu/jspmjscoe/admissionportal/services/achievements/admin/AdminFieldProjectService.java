package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.*;

public interface AdminFieldProjectService {

    // Single update
    FieldProjectSingleUpdateResultDTO updateFieldProject(FieldProjectUpdateRequestDTO request);

    // Bulk update
    FieldProjectBulkUpdateResultDTO updateFieldProjectsBulk(FieldProjectBulkUpdateRequestDTO bulkRequest);
}
