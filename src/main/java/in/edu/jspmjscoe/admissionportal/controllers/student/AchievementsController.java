package in.edu.jspmjscoe.admissionportal.controllers.student;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.MyAchievementsDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.apiresponse.ApiResponse;
import in.edu.jspmjscoe.admissionportal.services.achievements.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/student/achievements")
@RequiredArgsConstructor
public class AchievementsController {

    private final AchievementsService achievementsService;

    @PostMapping("/upload-certificate")
    public ResponseEntity<ApiResponse<CertificateDTO>> uploadCertificate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") CertificateDTO dto,
            @RequestPart("file") MultipartFile file) {

        CertificateDTO saved = achievementsService.uploadCertificate(userDetails, dto, file);
        return new ResponseEntity<>(new ApiResponse<>(201, "Certificate uploaded successfully", saved), HttpStatus.CREATED);
    }

    @PostMapping("/upload-internship")
    public ResponseEntity<ApiResponse<InternshipDTO>> uploadInternship(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") InternshipDTO dto,
            @RequestPart("file") MultipartFile file) {

        InternshipDTO saved = achievementsService.uploadInternship(userDetails, dto, file);
        return new ResponseEntity<>(new ApiResponse<>(201, "Internship uploaded successfully", saved), HttpStatus.CREATED);
    }

    @PostMapping("/upload-competition")
    public ResponseEntity<ApiResponse<CompetitionDTO>> uploadCompetition(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") CompetitionDTO dto,
            @RequestPart("file") MultipartFile file) {

        CompetitionDTO saved = achievementsService.uploadCompetition(userDetails, dto, file);
        return new ResponseEntity<>(new ApiResponse<>(201, "Competition uploaded successfully", saved), HttpStatus.CREATED);
    }

    @PostMapping("/upload-mini-project")
    public ResponseEntity<ApiResponse<MiniProjectDTO>> uploadMiniProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") MiniProjectDTO dto,
            @RequestPart("video") MultipartFile video,
            @RequestPart("pdf") MultipartFile pdf) {

        MiniProjectDTO saved = achievementsService.uploadMiniProject(userDetails, dto, video, pdf);
        return new ResponseEntity<>(new ApiResponse<>(201, "Mini Project uploaded successfully", saved), HttpStatus.CREATED);
    }

    @PostMapping("/upload-field-project")
    public ResponseEntity<ApiResponse<FieldProjectDTO>> uploadFieldProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") FieldProjectDTO dto,
            @RequestPart("video") MultipartFile video,
            @RequestPart("pdf") MultipartFile pdf) {

        FieldProjectDTO saved = achievementsService.uploadFieldProject(userDetails, dto, video, pdf);
        return new ResponseEntity<>(new ApiResponse<>(201, "Field Project uploaded successfully", saved), HttpStatus.CREATED);
    }

    @GetMapping("/my-achievements")
    public ResponseEntity<ApiResponse<MyAchievementsDTO>> getMyAchievements(
            @AuthenticationPrincipal UserDetails userDetails) {

        MyAchievementsDTO achievementsDTO = achievementsService.getMyAchievements(userDetails);
        return ResponseEntity.ok(new ApiResponse<>(200, "Achievements fetched successfully", achievementsDTO));
    }
}


