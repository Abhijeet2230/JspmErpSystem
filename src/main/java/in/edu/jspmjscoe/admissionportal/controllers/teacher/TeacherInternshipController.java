package in.edu.jspmjscoe.admissionportal.controllers.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.internship.*;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import in.edu.jspmjscoe.admissionportal.services.internship.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Teacher Controller for Internship Management
 * 
 * Teacher Role Permissions:
 * - Can CREATE (add) new internship postings and placements
 * - Can VIEW (read) all internship and placement data
 * - Cannot UPDATE or DELETE existing records
 * - Can view applications but cannot modify them
 * - Can add guest lectures, workshops, and industrial visits
 */
@RestController
@RequestMapping("/api/teacher/internships")
@RequiredArgsConstructor
public class TeacherInternshipController {

    private final CompanyService companyService;
    private final InternshipPostingService internshipPostingService;
    private final PlacementService placementService;
    private final InternshipApplicationService internshipApplicationService;
    private final PlacementApplicationService placementApplicationService;
    private final ConsultancyProjectService consultancyProjectService;
    private final GuestLectureService guestLectureService;
    private final TrainingSkillWorkshopService trainingSkillWorkshopService;
    private final IndustrialVisitService industrialVisitService;
    private final StudentInternshipProfileService studentInternshipProfileService;
    private final ResumeService resumeService;

    // ==================== COMPANY VIEWING ====================
    
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(company -> ResponseEntity.ok(company))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/companies/search")
    public ResponseEntity<List<CompanyDTO>> searchCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String hiringStatus) {
        
        if (name != null) {
            return ResponseEntity.ok(companyService.getCompaniesByName(name));
        } else if (industry != null) {
            return ResponseEntity.ok(companyService.getCompaniesByIndustry(industry));
        } else if (hiringStatus != null) {
            return ResponseEntity.ok(companyService.getCompaniesByHiringStatus(hiringStatus));
        }
        
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    // ==================== INTERNSHIP POSTING MANAGEMENT ====================
    
    @PostMapping("/postings")
    public ResponseEntity<InternshipPostingDTO> createInternshipPosting(@RequestBody InternshipPostingDTO postingDTO) {
        InternshipPostingDTO createdPosting = internshipPostingService.createPosting(postingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPosting);
    }

    @GetMapping("/postings")
    public ResponseEntity<List<InternshipPostingDTO>> getAllInternshipPostings() {
        List<InternshipPostingDTO> postings = internshipPostingService.getAllPostings();
        return ResponseEntity.ok(postings);
    }

    @GetMapping("/postings/effective-values")
    public ResponseEntity<List<InternshipPostingWithEffectiveValuesDTO>> getAllInternshipPostingsWithEffectiveValues() {
        List<InternshipPostingWithEffectiveValuesDTO> postings = internshipPostingService.getAllPostingsWithEffectiveValues();
        return ResponseEntity.ok(postings);
    }

    @GetMapping("/postings/{id}")
    public ResponseEntity<InternshipPostingDTO> getInternshipPostingById(@PathVariable Long id) {
        return internshipPostingService.getPostingById(id)
                .map(posting -> ResponseEntity.ok(posting))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/postings/{id}/effective-values")
    public ResponseEntity<InternshipPostingWithEffectiveValuesDTO> getInternshipPostingWithEffectiveValues(@PathVariable Long id) {
        return internshipPostingService.getPostingWithEffectiveValues(id)
                .map(posting -> ResponseEntity.ok(posting))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/postings/search")
    public ResponseEntity<List<InternshipPostingDTO>> searchInternshipPostings(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) PostingStatus status) {
        
        if (companyId != null) {
            return ResponseEntity.ok(internshipPostingService.getPostingsByCompany(companyId));
        } else if (title != null) {
            return ResponseEntity.ok(internshipPostingService.getPostingsByTitle(title));
        } else if (status != null) {
            return ResponseEntity.ok(internshipPostingService.getPostingsByStatus(status));
        }
        
        return ResponseEntity.ok(internshipPostingService.getAllPostings());
    }

    @GetMapping("/postings/active")
    public ResponseEntity<List<InternshipPostingDTO>> getActiveInternshipPostings() {
        List<InternshipPostingDTO> activePostings = internshipPostingService.getActivePostings();
        return ResponseEntity.ok(activePostings);
    }

    // ==================== PLACEMENT MANAGEMENT ====================
    
    @PostMapping("/placements")
    public ResponseEntity<PlacementDTO> createPlacement(@RequestBody PlacementDTO placementDTO) {
        PlacementDTO createdPlacement = placementService.createPlacement(placementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlacement);
    }

    @GetMapping("/placements")
    public ResponseEntity<List<PlacementDTO>> getAllPlacements() {
        List<PlacementDTO> placements = placementService.getAllPlacements();
        return ResponseEntity.ok(placements);
    }

    @GetMapping("/placements/effective-values")
    public ResponseEntity<List<PlacementWithEffectiveValuesDTO>> getAllPlacementsWithEffectiveValues() {
        List<PlacementWithEffectiveValuesDTO> placements = placementService.getAllPlacementsWithEffectiveValues();
        return ResponseEntity.ok(placements);
    }

    @GetMapping("/placements/{id}")
    public ResponseEntity<PlacementDTO> getPlacementById(@PathVariable Long id) {
        return placementService.getPlacementById(id)
                .map(placement -> ResponseEntity.ok(placement))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/placements/{id}/effective-values")
    public ResponseEntity<PlacementWithEffectiveValuesDTO> getPlacementWithEffectiveValues(@PathVariable Long id) {
        return placementService.getPlacementWithEffectiveValues(id)
                .map(placement -> ResponseEntity.ok(placement))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/placements/search")
    public ResponseEntity<List<PlacementDTO>> searchPlacements(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) PostingStatus status) {
        
        if (companyId != null) {
            return ResponseEntity.ok(placementService.getPlacementsByCompany(companyId));
        } else if (jobTitle != null) {
            return ResponseEntity.ok(placementService.getPlacementsByJobTitle(jobTitle));
        } else if (status != null) {
            return ResponseEntity.ok(placementService.getPlacementsByStatus(status));
        }
        
        return ResponseEntity.ok(placementService.getAllPlacements());
    }

    @GetMapping("/placements/active")
    public ResponseEntity<List<PlacementDTO>> getActivePlacements() {
        List<PlacementDTO> activePlacements = placementService.getActivePlacements();
        return ResponseEntity.ok(activePlacements);
    }

    // ==================== APPLICATION VIEWING ====================
    
    @GetMapping("/internship-applications")
    public ResponseEntity<List<InternshipApplicationDTO>> getAllInternshipApplications() {
        List<InternshipApplicationDTO> applications = internshipApplicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/internship-applications/{id}")
    public ResponseEntity<InternshipApplicationDTO> getInternshipApplicationById(@PathVariable Long id) {
        return internshipApplicationService.getApplicationById(id)
                .map(application -> ResponseEntity.ok(application))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/internship-applications/posting/{postingId}")
    public ResponseEntity<List<InternshipApplicationDTO>> getInternshipApplicationsByPosting(@PathVariable Long postingId) {
        List<InternshipApplicationDTO> applications = internshipApplicationService.getApplicationsByInternship(postingId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/placement-applications")
    public ResponseEntity<List<PlacementApplicationDTO>> getAllPlacementApplications() {
        List<PlacementApplicationDTO> applications = placementApplicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/placement-applications/{id}")
    public ResponseEntity<PlacementApplicationDTO> getPlacementApplicationById(@PathVariable Long id) {
        return placementApplicationService.getApplicationById(id)
                .map(application -> ResponseEntity.ok(application))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/placement-applications/placement/{placementId}")
    public ResponseEntity<List<PlacementApplicationDTO>> getPlacementApplicationsByPlacement(@PathVariable Long placementId) {
        List<PlacementApplicationDTO> applications = placementApplicationService.getApplicationsByPlacement(placementId);
        return ResponseEntity.ok(applications);
    }

    // ==================== CONSULTANCY PROJECT VIEWING ====================
    
    @GetMapping("/consultancy-projects")
    public ResponseEntity<List<ConsultancyProjectWorkDTO>> getAllConsultancyProjects() {
        List<ConsultancyProjectWorkDTO> projects = consultancyProjectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/consultancy-projects/{id}")
    public ResponseEntity<ConsultancyProjectWorkDTO> getConsultancyProjectById(@PathVariable Long id) {
        return consultancyProjectService.getProjectById(id)
                .map(project -> ResponseEntity.ok(project))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/consultancy-projects/active")
    public ResponseEntity<List<ConsultancyProjectWorkDTO>> getActiveConsultancyProjects() {
        List<ConsultancyProjectWorkDTO> activeProjects = consultancyProjectService.getActiveProjects();
        return ResponseEntity.ok(activeProjects);
    }

    // ==================== GUEST LECTURE MANAGEMENT ====================
    
    @PostMapping("/guest-lectures")
    public ResponseEntity<GuestLectureDTO> createGuestLecture(@RequestBody GuestLectureDTO guestLectureDTO) {
        GuestLectureDTO createdLecture = guestLectureService.createGuestLecture(guestLectureDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLecture);
    }

    @GetMapping("/guest-lectures")
    public ResponseEntity<List<GuestLectureDTO>> getAllGuestLectures() {
        List<GuestLectureDTO> lectures = guestLectureService.getAllGuestLectures();
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/guest-lectures/{id}")
    public ResponseEntity<GuestLectureDTO> getGuestLectureById(@PathVariable Long id) {
        return guestLectureService.getGuestLectureById(id)
                .map(lecture -> ResponseEntity.ok(lecture))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guest-lectures/search")
    public ResponseEntity<List<GuestLectureDTO>> searchGuestLectures(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String speaker,
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) LocalDate date) {
        
        if (title != null) {
            return ResponseEntity.ok(guestLectureService.getGuestLecturesByTitle(title));
        } else if (speaker != null) {
            return ResponseEntity.ok(guestLectureService.getGuestLecturesBySpeaker(speaker));
        } else if (organization != null) {
            return ResponseEntity.ok(guestLectureService.getGuestLecturesByOrganization(organization));
        } else if (date != null) {
            return ResponseEntity.ok(guestLectureService.getGuestLecturesByDate(date));
        }
        
        return ResponseEntity.ok(guestLectureService.getAllGuestLectures());
    }

    // ==================== TRAINING WORKSHOP MANAGEMENT ====================
    
    @PostMapping("/training-workshops")
    public ResponseEntity<TrainingSkillWorkshopDTO> createTrainingWorkshop(@RequestBody TrainingSkillWorkshopDTO workshopDTO) {
        TrainingSkillWorkshopDTO createdWorkshop = trainingSkillWorkshopService.createWorkshop(workshopDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkshop);
    }

    @GetMapping("/training-workshops")
    public ResponseEntity<List<TrainingSkillWorkshopDTO>> getAllTrainingWorkshops() {
        List<TrainingSkillWorkshopDTO> workshops = trainingSkillWorkshopService.getAllWorkshops();
        return ResponseEntity.ok(workshops);
    }

    @GetMapping("/training-workshops/{id}")
    public ResponseEntity<TrainingSkillWorkshopDTO> getTrainingWorkshopById(@PathVariable Long id) {
        return trainingSkillWorkshopService.getWorkshopById(id)
                .map(workshop -> ResponseEntity.ok(workshop))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/training-workshops/search")
    public ResponseEntity<List<TrainingSkillWorkshopDTO>> searchTrainingWorkshops(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) LocalDate date) {
        
        if (title != null) {
            return ResponseEntity.ok(trainingSkillWorkshopService.getWorkshopsByTitle(title));
        } else if (organization != null) {
            return ResponseEntity.ok(trainingSkillWorkshopService.getWorkshopsByOrganization(organization));
        } else if (date != null) {
            return ResponseEntity.ok(trainingSkillWorkshopService.getWorkshopsByDate(date));
        }
        
        return ResponseEntity.ok(trainingSkillWorkshopService.getAllWorkshops());
    }

    // ==================== INDUSTRIAL VISIT MANAGEMENT ====================
    
    @PostMapping("/industrial-visits")
    public ResponseEntity<IndustrialVisitDTO> createIndustrialVisit(@RequestBody IndustrialVisitDTO industrialVisitDTO) {
        IndustrialVisitDTO createdVisit = industrialVisitService.createIndustrialVisit(industrialVisitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVisit);
    }

    @GetMapping("/industrial-visits")
    public ResponseEntity<List<IndustrialVisitDTO>> getAllIndustrialVisits() {
        List<IndustrialVisitDTO> visits = industrialVisitService.getAllIndustrialVisits();
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/industrial-visits/{id}")
    public ResponseEntity<IndustrialVisitDTO> getIndustrialVisitById(@PathVariable Long id) {
        return industrialVisitService.getIndustrialVisitById(id)
                .map(visit -> ResponseEntity.ok(visit))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/industrial-visits/search")
    public ResponseEntity<List<IndustrialVisitDTO>> searchIndustrialVisits(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) LocalDate date) {
        
        if (title != null) {
            return ResponseEntity.ok(industrialVisitService.getIndustrialVisitsByTitle(title));
        } else if (organization != null) {
            return ResponseEntity.ok(industrialVisitService.getIndustrialVisitsByOrganization(organization));
        } else if (date != null) {
            return ResponseEntity.ok(industrialVisitService.getIndustrialVisitsByDate(date));
        }
        
        return ResponseEntity.ok(industrialVisitService.getAllIndustrialVisits());
    }

    // ==================== STUDENT PROFILE VIEWING ====================
    
    @GetMapping("/student-profiles")
    public ResponseEntity<List<StudentProfileDTO>> getAllStudentProfiles() {
        List<StudentProfileDTO> profiles = studentInternshipProfileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/student-profiles/{id}")
    public ResponseEntity<StudentProfileDTO> getStudentProfileById(@PathVariable Long id) {
        return studentInternshipProfileService.getProfileById(id)
                .map(profile -> ResponseEntity.ok(profile))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student-profiles/student/{studentId}")
    public ResponseEntity<StudentProfileDTO> getStudentProfileByStudentId(@PathVariable Long studentId) {
        return studentInternshipProfileService.getProfileByStudentId(studentId)
                .map(profile -> ResponseEntity.ok(profile))
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== RESUME VIEWING FOR TEACHERS ====================
    
    @GetMapping("/student-profiles/{studentId}/resume/info")
    public ResponseEntity<?> getStudentResumeInfo(@PathVariable Long studentId) {
        try {
            var resumeInfo = resumeService.getResumeFileInfo(studentId);
            return ResponseEntity.ok(resumeInfo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(Map.of(
                "hasResume", false,
                "studentId", studentId,
                "message", "No resume uploaded by this student"
            ));
        }
    }

    @GetMapping("/student-profiles/{studentId}/resume/status")
    public ResponseEntity<?> getStudentResumeStatus(@PathVariable Long studentId) {
        boolean hasResume = resumeService.hasResume(studentId);
        return ResponseEntity.ok(Map.of(
            "hasResume", hasResume,
            "studentId", studentId
        ));
    }

    @GetMapping("/student-profiles/resumes/summary")
    public ResponseEntity<?> getResumesSummary() {
        List<StudentProfileDTO> allProfiles = studentInternshipProfileService.getAllProfiles();
        
        long totalStudents = allProfiles.size();
        long studentsWithResume = allProfiles.stream()
                .mapToLong(profile -> resumeService.hasResume(profile.getStudentId()) ? 1 : 0)
                .sum();
        
        double resumeUploadPercentage = totalStudents > 0 ? (double) studentsWithResume / totalStudents * 100 : 0;
        
        return ResponseEntity.ok(Map.of(
            "totalStudents", totalStudents,
            "studentsWithResume", studentsWithResume,
            "studentsWithoutResume", totalStudents - studentsWithResume,
            "resumeUploadPercentage", Math.round(resumeUploadPercentage * 100.0) / 100.0
        ));
    }
}