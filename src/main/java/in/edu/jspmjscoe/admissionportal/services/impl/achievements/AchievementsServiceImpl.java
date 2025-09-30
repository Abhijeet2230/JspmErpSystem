package in.edu.jspmjscoe.admissionportal.services.impl.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.*;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AchievementsServiceImpl implements AchievementsService {

    private final CertificateService certificateService;
    private final InternshipService internshipService;
    private final CompetitionService competitionService;
    private final MiniProjectService miniProjectService;
    private final FieldProjectService fieldProjectService;
    private final MinioStorageService minioStorageService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AchievementViewService achievementViewService;
    private final StudentAcademicYearRepository studentAcademicYearRepository;

    // ------------------ COMMON HELPER ------------------
    private StudentAcademicYear getActiveAcademicYear(UserDetails userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return studentAcademicYearRepository
                .findByStudent_StudentIdAndIsActiveTrue(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Active Academic Year not found"));
    }

    // ------------------ CERTIFICATE ------------------
    @Override
    public CertificateDTO uploadCertificate(UserDetails userDetails, CertificateDTO dto, MultipartFile file) {
        StudentAcademicYear academicYear = getActiveAcademicYear(userDetails);

        String objectKey = minioStorageService.generateObjectKey(
                "certificates",
                academicYear.getStudentAcademicYearId(),
                file.getOriginalFilename()
        );
        minioStorageService.uploadFile(file, objectKey);
        dto.setMinioObjectKey(objectKey);

        return certificateService.saveCertificate(dto, academicYear);
    }

    // ------------------ INTERNSHIP ------------------
    @Override
    public InternshipDTO uploadInternship(UserDetails userDetails, InternshipDTO dto, MultipartFile file) {
        StudentAcademicYear academicYear = getActiveAcademicYear(userDetails);

        String objectKey = minioStorageService.generateObjectKey(
                "internships",
                academicYear.getStudentAcademicYearId(),
                file.getOriginalFilename()
        );
        minioStorageService.uploadFile(file, objectKey);
        dto.setMinioObjectKey(objectKey);

        return internshipService.saveInternship(dto, academicYear);
    }

    // ------------------ COMPETITION ------------------
    @Override
    public CompetitionDTO uploadCompetition(UserDetails userDetails, CompetitionDTO dto, MultipartFile file) {
        StudentAcademicYear academicYear = getActiveAcademicYear(userDetails);

        String objectKey = minioStorageService.generateObjectKey(
                "competitions",
                academicYear.getStudentAcademicYearId(),
                file.getOriginalFilename()
        );
        minioStorageService.uploadFile(file, objectKey);
        dto.setMinioObjectKey(objectKey);

        return competitionService.saveCompetition(dto, academicYear);
    }

    // ------------------ MINI PROJECT ------------------
    @Override
    public MiniProjectDTO uploadMiniProject(UserDetails userDetails, MiniProjectDTO dto, MultipartFile video, MultipartFile pdf) {
        StudentAcademicYear academicYear = getActiveAcademicYear(userDetails);

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

        minioStorageService.uploadFile(video, videoKey);
        minioStorageService.uploadFile(pdf, pdfKey);

        dto.setVideoMinioKey(videoKey);
        dto.setPdfMinioKey(pdfKey);

        return miniProjectService.saveMiniProject(dto, academicYear);
    }

    // ------------------ FIELD PROJECT ------------------
    @Override
    public FieldProjectDTO uploadFieldProject(UserDetails userDetails, FieldProjectDTO dto, MultipartFile video, MultipartFile pdf) {
        StudentAcademicYear academicYear = getActiveAcademicYear(userDetails);

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

        minioStorageService.uploadFile(video, videoKey);
        minioStorageService.uploadFile(pdf, pdfKey);

        dto.setVideoMinioKey(videoKey);
        dto.setPdfMinioKey(pdfKey);

        return fieldProjectService.saveFieldProject(dto, academicYear);
    }

    // ------------------ VIEW ACHIEVEMENTS ------------------
    @Override
    public MyAchievementsDTO getMyAchievements(UserDetails userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        return MyAchievementsDTO.builder()
                .certificates(achievementViewService.getCertificates(student))
                .internships(achievementViewService.getInternships(student))
                .competitions(achievementViewService.getCompetitions(student))
                .miniProjects(achievementViewService.getMiniProjects(student))
                .fieldProjects(achievementViewService.getFieldProjects(student))
                .build();
    }
}
