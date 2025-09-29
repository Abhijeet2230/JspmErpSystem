package in.edu.jspmjscoe.admissionportal.controllers.student;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.MyAchievementsDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.apiresponse.ApiResponse;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.*;
import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
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

    private final CertificateService certificateService;
    private final InternshipService internshipService;
    private final CompetitionService competitionService;
    private final MiniProjectService miniProjectService;
    private final FieldProjectService fieldProjectService;
    private final MinioStorageService minioStorageService;
    private final UserRepository  userRepository;
    private final StudentRepository studentRepository;
    private final AchievementViewService achievementViewService;
    private final StudentAcademicYearRepository studentAcademicYearRepository;

    @PostMapping("/upload-certificate")
    public ResponseEntity<ApiResponse<CertificateDTO>> uploadCertificate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") CertificateDTO certificateDTO,
            @RequestPart("file") MultipartFile file
    ) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        StudentAcademicYear academicYear = studentAcademicYearRepository
                .findByStudent_StudentIdAndIsActiveTrue(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Active Academic Year not found"));

        String objectKey = minioStorageService.generateObjectKey(
                "certificates",
                academicYear.getStudentAcademicYearId(), // ✅ correct getter
                file.getOriginalFilename()
        );
        minioStorageService.uploadFile(file, objectKey);

        certificateDTO.setMinioObjectKey(objectKey);

        CertificateDTO saved = certificateService.saveCertificate(certificateDTO, academicYear);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(),
                "Certificate uploaded successfully", saved), HttpStatus.CREATED);
    }

    // ------------------ INTERNSHIP ------------------
    @PostMapping("/upload-internship")
    public ResponseEntity<ApiResponse<InternshipDTO>> uploadInternship(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") InternshipDTO internshipDTO,
            @RequestPart("file") MultipartFile file
    ) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        StudentAcademicYear academicYear = studentAcademicYearRepository
                .findByStudent_StudentIdAndIsActiveTrue(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Active Academic Year not found"));

        String objectKey = minioStorageService.generateObjectKey(
                "internships",
                academicYear.getStudentAcademicYearId(),
                file.getOriginalFilename()
        );
        minioStorageService.uploadFile(file, objectKey);
        internshipDTO.setMinioObjectKey(objectKey);

        InternshipDTO saved = internshipService.saveInternship(internshipDTO, academicYear);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(),
                "Internship uploaded successfully", saved), HttpStatus.CREATED);
    }


    // ------------------ COMPETITION ------------------
    @PostMapping("/upload-competition")
    public ResponseEntity<ApiResponse<CompetitionDTO>> uploadCompetition(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") CompetitionDTO competitionDTO,
            @RequestPart("file") MultipartFile file
    ) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        StudentAcademicYear academicYear = studentAcademicYearRepository
                .findByStudent_StudentIdAndIsActiveTrue(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Active Academic Year not found"));

        String objectKey = minioStorageService.generateObjectKey(
                "competitions",
                academicYear.getStudentAcademicYearId(),
                file.getOriginalFilename()
        );
        minioStorageService.uploadFile(file, objectKey);
        competitionDTO.setMinioObjectKey(objectKey);

        CompetitionDTO saved = competitionService.saveCompetition(competitionDTO, academicYear);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(),
                "Competition uploaded successfully", saved), HttpStatus.CREATED);
    }

    // ------------------ MINI PROJECT ------------------
    @PostMapping("/upload-mini-project")
    public ResponseEntity<ApiResponse<MiniProjectDTO>> uploadMiniProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") MiniProjectDTO miniProjectDTO,
            @RequestPart("video") MultipartFile video,
            @RequestPart("pdf") MultipartFile pdf
    ) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        StudentAcademicYear academicYear = studentAcademicYearRepository
                .findByStudent_StudentIdAndIsActiveTrue(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Active Academic Year not found"));

        // Generate object keys
        String videoKey = minioStorageService.generateObjectKey(
                "mini-projects/video",
                academicYear.getStudentAcademicYearId(),
                video.getOriginalFilename()
        );
        String pdfKey = minioStorageService.generateObjectKey(
                "mini-projects/pdf",
                academicYear.getStudentAcademicYearId(),
                pdf.getOriginalFilename()
        );

        // Upload
        minioStorageService.uploadFile(video, videoKey);
        minioStorageService.uploadFile(pdf, pdfKey);

        miniProjectDTO.setVideoMinioKey(videoKey);
        miniProjectDTO.setPdfMinioKey(pdfKey);

        MiniProjectDTO saved = miniProjectService.saveMiniProject(miniProjectDTO, academicYear);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(),
                "Mini Project uploaded successfully", saved), HttpStatus.CREATED);
    }


    // ------------------ FIELD PROJECT ------------------
    @PostMapping("/upload-field-project")
    public ResponseEntity<ApiResponse<FieldProjectDTO>> uploadFieldProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("metadata") FieldProjectDTO fieldProjectDTO,
            @RequestPart("video") MultipartFile video,
            @RequestPart("pdf") MultipartFile pdf
    ) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        StudentAcademicYear academicYear = studentAcademicYearRepository
                .findByStudent_StudentIdAndIsActiveTrue(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Active Academic Year not found"));

        // Generate object keys
        String videoKey = minioStorageService.generateObjectKey(
                "field-projects/video",
                academicYear.getStudentAcademicYearId(),
                video.getOriginalFilename()
        );
        String pdfKey = minioStorageService.generateObjectKey(
                "field-projects/pdf",
                academicYear.getStudentAcademicYearId(),
                pdf.getOriginalFilename()
        );

        // Upload
        minioStorageService.uploadFile(video, videoKey);
        minioStorageService.uploadFile(pdf, pdfKey);

        fieldProjectDTO.setVideoMinioKey(videoKey);
        fieldProjectDTO.setPdfMinioKey(pdfKey);

        FieldProjectDTO saved = fieldProjectService.saveFieldProject(fieldProjectDTO, academicYear);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(),
                "Field Project uploaded successfully", saved), HttpStatus.CREATED);
    }



    @GetMapping("/my-achievements")
    public ResponseEntity<ApiResponse<MyAchievementsDTO>> getMyAchievements(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized: Login required");
        }

        // Find logged-in user
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Find student linked to user
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        // Fetch all achievements
        MyAchievementsDTO achievementsDTO = MyAchievementsDTO.builder()
                .certificates(achievementViewService.getCertificates(student))
                .internships(achievementViewService.getInternships(student))
                .competitions(achievementViewService.getCompetitions(student))
                .miniProjects(achievementViewService.getMiniProjects(student))
                .fieldProjects(achievementViewService.getFieldProjects(student))
                .build();

        ApiResponse<MyAchievementsDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Achievements fetched successfully",
                achievementsDTO
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
