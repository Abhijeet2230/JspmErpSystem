package in.edu.jspmjscoe.admissionportal.controllers.admin.achievement;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.admin.AdminAchievementResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateMarksBulkUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionMarksBulkUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.request.FieldProjectBulkUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.response.FieldProjectBulkUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipMarksBulkUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.apiresponse.ApiResponse;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/achievements")
@RequiredArgsConstructor
public class AdminAchievementController {

    private final AdminAchievementService adminAchievementService;
    private final AdminCertificateService adminCertificateService;
    private final AdminInternshipService adminInternshipService;
    private final AdminCompetitionService adminCompetitionService;
    private final AdminFieldProjectService adminFieldProjectService;

    /**
     * Fetch achievements dynamically based on filters.
     *
     * @param achievementType Type of achievement: Certificate, Internship, Competition, MiniProject, FieldProject
     * @param yearOfStudy     Optional academic year of student
     * @param semester        Optional semester
     * @param department      Optional department
     * @param course          Optional course
     * @param division        Optional division
     * @param subjectId       Optional subject ID (only for FieldProject or other subject-specific achievements)
     * @return AdminAchievementResponseDTO containing student achievements
     */
    @GetMapping
    public AdminAchievementResponseDTO<?> getAchievements(
            @RequestParam String achievementType,
            @RequestParam(required = false) Integer yearOfStudy,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) String division,
            @RequestParam(required = false) Long subjectId
    ) {
        return adminAchievementService.getAchievements(
                achievementType,
                yearOfStudy,
                semester,
                department,
                course,
                division,
                subjectId
        );
    }

    @PatchMapping("/certificates/marks/bulk")
    public ResponseEntity<ApiResponse<CertificateUpdateResultDTO>> updateCertificateMarksBulk(
            @RequestBody CertificateMarksBulkUpdateRequestDTO request) {

        CertificateUpdateResultDTO result = adminCertificateService
                .assignCertificateMarksBulk(request.getUpdates());

        ApiResponse<CertificateUpdateResultDTO> response = new ApiResponse<>(
                200,
                "Certificate marks updated and TrainingPlacementRecord recalculated successfully",
                result
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/internships/marks/bulk")
    public ResponseEntity<ApiResponse<InternshipUpdateResultDTO>> updateInternshipMarksBulk(
            @RequestBody InternshipMarksBulkUpdateRequestDTO request) {

        InternshipUpdateResultDTO result = adminInternshipService
                .assignInternshipMarksBulk(request.getUpdates());

        ApiResponse<InternshipUpdateResultDTO> response = new ApiResponse<>(
                200,
                "Internship marks updated successfully",
                result
        );

        return ResponseEntity.ok(response);
    }



    @PatchMapping("/competitions/marks/bulk")
    public ResponseEntity<ApiResponse<CompetitionUpdateResultDTO>> updateCompetitionMarksBulk(
            @RequestBody CompetitionMarksBulkUpdateRequestDTO request) {

        CompetitionUpdateResultDTO result = adminCompetitionService
                .assignCompetitionMarksBulk(request.getUpdates());

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Competition marks updated successfully", result)
        );
    }


    @PatchMapping("/fieldprojects/marks/bulk")
    public ResponseEntity<ApiResponse<FieldProjectBulkUpdateResultDTO>> updateFieldProjectMarksBulk(
            @RequestBody FieldProjectBulkUpdateRequestDTO bulkRequest) {

        FieldProjectBulkUpdateResultDTO result = adminFieldProjectService.updateFieldProjectsBulk(bulkRequest);

        ApiResponse<FieldProjectBulkUpdateResultDTO> response = new ApiResponse<>(
                200,
                "Field project marks updated successfully",
                result
        );

        return ResponseEntity.ok(response);
    }

}

