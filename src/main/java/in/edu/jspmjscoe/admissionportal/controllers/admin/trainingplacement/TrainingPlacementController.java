package in.edu.jspmjscoe.admissionportal.controllers.admin.trainingplacement;

import in.edu.jspmjscoe.admissionportal.dtos.apiresponse.ApiResponse;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.BulkTrainingPlacementPatchRequest;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.StudentPlacementDTO;
import in.edu.jspmjscoe.admissionportal.services.trainingplacement.TrainingPlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/admin/training-placement")
@RequiredArgsConstructor
public class TrainingPlacementController {

    private final TrainingPlacementService trainingPlacementService;

    /**
     * Initialize training & placement records with default values.
     */
    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<Void>> initializeRecords() {
        trainingPlacementService.initializeTrainingPlacementRecords();
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Training & Placement records initialized with 0.0 and default tests.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all students by division.
     */
    @GetMapping("/division/{division}")
    public ResponseEntity<ApiResponse<List<StudentPlacementDTO>>> getStudentsByDivision(
            @PathVariable String division) {

        List<StudentPlacementDTO> students = trainingPlacementService.getStudentsByDivision(division)
                .stream()
                .sorted(Comparator.comparingInt(StudentPlacementDTO::getRollNo))
                .toList(); // Java 16+; for earlier, use .collect(Collectors.toList())

        // If rollNo is String and you want numeric sort, use:
        // students.sort(Comparator.comparingInt(s -> Integer.parseInt(s.getRollNo())));

        ApiResponse<List<StudentPlacementDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Students fetched successfully for division: " + division,
                students
        );

        return ResponseEntity.ok(response);
    }


    /**
     * Bulk update of training placement records.
     */
    @PatchMapping("/bulk-update")
    public ResponseEntity<ApiResponse<String>> bulkUpdate(
            @RequestBody BulkTrainingPlacementPatchRequest request) {
        trainingPlacementService.bulkPatch(request);
        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Bulk training placement records updated successfully.",
                null // or any optional info/ID list you want to return
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping("/rollno/{rollNo}")
    public ResponseEntity<ApiResponse<List<StudentPlacementDTO>>> getStudentsByRollNo(
            @PathVariable Integer rollNo) {

        List<StudentPlacementDTO> students = trainingPlacementService.getStudentsByRollNo(rollNo);

        ApiResponse<List<StudentPlacementDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Students fetched successfully for rollNo: " + rollNo,
                students
        );

        return ResponseEntity.ok(response);
    }



}
