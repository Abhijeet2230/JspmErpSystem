package in.edu.jspmjscoe.admissionportal.services.trainingplacement;

import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.BulkTrainingPlacementPatchRequest;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.StudentPlacementDTO;

import java.util.List;

public interface TrainingPlacementService {

    void initializeTrainingPlacementRecords();

    List<StudentPlacementDTO> getStudentsByDivision(String division);

    void bulkPatch(BulkTrainingPlacementPatchRequest request);

    StudentPlacementDTO getMyTrainingPlacement(Long studentId);
}
