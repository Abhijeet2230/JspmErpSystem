package in.edu.jspmjscoe.admissionportal.controllers.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.admin.AdminAchievementResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.apiresponse.ApiResponse;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminAchievementService;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminCertificateService;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminCompetitionService;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminInternshipService;
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
            @RequestBody Map<Long, Double> certificateMarks) {
        CertificateUpdateResultDTO result = adminCertificateService.assignCertificateMarksBulk(certificateMarks);

        ApiResponse<CertificateUpdateResultDTO> response = new ApiResponse<>(
                200,
                "Certificate marks updated and TrainingPlacementRecord recalculated successfully",
                result
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/internships/marks/bulk")
    public ResponseEntity<ApiResponse<InternshipUpdateResultDTO>> updateInternshipMarksBulk(
            @RequestBody Map<Long, Double> internshipMarks) {


        InternshipUpdateResultDTO result = adminInternshipService.assignInternshipMarksBulk(internshipMarks);

        ApiResponse<InternshipUpdateResultDTO> response = new ApiResponse<>(
                200,
                "Internship marks updated successfully",
                result
        );

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/competitions/marks/bulk")
    public ResponseEntity<ApiResponse<CompetitionUpdateResultDTO>> updateCompetitionMarksBulk(
            @RequestBody Map<Long, Double> competitionMarks) {

        CompetitionUpdateResultDTO result = adminCompetitionService.assignCompetitionMarksBulk(competitionMarks);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Competition marks updated successfully", result)
        );
    }



}

