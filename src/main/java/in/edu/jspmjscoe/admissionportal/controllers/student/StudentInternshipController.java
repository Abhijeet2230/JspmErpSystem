package in.edu.jspmjscoe.admissionportal.controllers.student;

import in.edu.jspmjscoe.admissionportal.dtos.internship.*;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.security.UnauthorizedException;
import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Student Controller for Internship Management
 * 
 * Student Role Permissions:
 * - Can VIEW available internships, placements, guest lectures, workshops
 * - Can APPLY for internships and placements
 * - Can view their own applications and application status
 * - Cannot CREATE, UPDATE, or DELETE any records
 * - Can view their own profile and update it
 * - Can register for guest lectures, workshops, and industrial visits
 */
@RestController
@RequestMapping("/api/student/internships")
@RequiredArgsConstructor
public class StudentInternshipController {

    private final CompanyService companyService;
    private final InternshipPostingService internshipPostingService;
    private final PlacementService placementService;
    private final InternshipApplicationService internshipApplicationService;
    private final PlacementApplicationService placementApplicationService;
    private final ConsultancyProjectService consultancyProjectService;
    private final ConsultancyProjectApplicationService consultancyProjectApplicationService;
    private final GuestLectureService guestLectureService;
    private final TrainingSkillWorkshopService trainingSkillWorkshopService;
    private final IndustrialVisitService industrialVisitService;
    private final StudentInternshipProfileService studentProfileService;
    private final ResumeService resumeService;
    private final ApplicationFormService applicationFormService;
    
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    // ==================== HELPER METHOD ====================
    
    private Student getCurrentStudent(UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized: Login required");
        }

        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return studentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
    }

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
            @RequestParam(required = false) String industry) {
        
        if (name != null) {
            return ResponseEntity.ok(companyService.getCompaniesByName(name));
        } else if (industry != null) {
            return ResponseEntity.ok(companyService.getCompaniesByIndustry(industry));
        }
        
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    // ==================== INTERNSHIP VIEWING & APPLICATION ====================
    
    @GetMapping("/postings")
    public ResponseEntity<List<InternshipPostingDTO>> getAllInternshipPostings() {
        List<InternshipPostingDTO> postings = internshipPostingService.getAllPostings();
        return ResponseEntity.ok(postings);
    }

    @GetMapping("/postings/active")
    public ResponseEntity<List<InternshipPostingDTO>> getActiveInternshipPostings() {
        List<InternshipPostingDTO> activePostings = internshipPostingService.getActivePostings();
        return ResponseEntity.ok(activePostings);
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
            @RequestParam(required = false) String title) {
        
        if (companyId != null) {
            return ResponseEntity.ok(internshipPostingService.getPostingsByCompany(companyId));
        } else if (title != null) {
            return ResponseEntity.ok(internshipPostingService.getPostingsByTitle(title));
        }
        
        return ResponseEntity.ok(internshipPostingService.getActivePostings());
    }

    @GetMapping("/postings/{id}/application-form")
    public ResponseEntity<?> getInternshipApplicationForm(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        try {
            // Check if student can apply
            if (!applicationFormService.canApplyForInternship(student.getStudentId(), id)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Cannot apply",
                    "message", "You have already applied for this internship or your profile is incomplete"
                ));
            }
            
            InternshipApplicationDTO formDTO = applicationFormService.getInternshipApplicationForm(student.getStudentId(), id);
            return ResponseEntity.ok(formDTO);
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/postings/{id}/apply")
    public ResponseEntity<?> applyForInternship(
            @PathVariable Long id,
            @RequestBody InternshipApplicationDTO applicationFormDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        StudentProfileDTO profile = studentProfileService.getProfileByStudentId(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
        
        try {
            // Set the student info and internship ID
            applicationFormDTO.setStudentId(student.getStudentId());
            applicationFormDTO.setInternshipId(id);
            applicationFormDTO.setProfileId(profile.getProfileId());
            
            InternshipApplicationDTO createdApplication = applicationFormService.submitInternshipApplication(applicationFormDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Application submitted successfully",
                "applicationId", createdApplication.getApplicationId(),
                "status", createdApplication.getStatus()
            ));
            
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(Map.of(
                "error", "Incomplete Profile",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Application Failed",
                "message", e.getMessage()
            ));
        }
    }

    // ==================== PLACEMENT VIEWING & APPLICATION ====================
    
    @GetMapping("/placements")
    public ResponseEntity<List<PlacementDTO>> getAllPlacements() {
        List<PlacementDTO> placements = placementService.getAllPlacements();
        return ResponseEntity.ok(placements);
    }

    @GetMapping("/placements/active")
    public ResponseEntity<List<PlacementDTO>> getActivePlacements() {
        List<PlacementDTO> activePlacements = placementService.getActivePlacements();
        return ResponseEntity.ok(activePlacements);
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
            @RequestParam(required = false) String jobTitle) {
        
        if (companyId != null) {
            return ResponseEntity.ok(placementService.getPlacementsByCompany(companyId));
        } else if (jobTitle != null) {
            return ResponseEntity.ok(placementService.getPlacementsByJobTitle(jobTitle));
        }
        
        return ResponseEntity.ok(placementService.getActivePlacements());
    }

    @GetMapping("/placements/{id}/application-form")
    public ResponseEntity<?> getPlacementApplicationForm(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        try {
            // Check if student can apply
            if (!applicationFormService.canApplyForPlacement(student.getStudentId(), id)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Cannot apply",
                    "message", "You have already applied for this placement or your profile is incomplete"
                ));
            }
            
            PlacementApplicationDTO formDTO = applicationFormService.getPlacementApplicationForm(student.getStudentId(), id);
            return ResponseEntity.ok(formDTO);
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/placements/{id}/apply")
    public ResponseEntity<?> applyForPlacement(
            @PathVariable Long id,
            @RequestBody PlacementApplicationDTO applicationFormDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        try {
            // Set the student info and placement ID
            applicationFormDTO.setStudentId(student.getStudentId());
            applicationFormDTO.setPlacementId(id);
            
            PlacementApplicationDTO createdApplication = applicationFormService.submitPlacementApplication(applicationFormDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Application submitted successfully",
                "applicationId", createdApplication.getApplicationId(),
                "status", createdApplication.getStatus()
            ));
            
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(Map.of(
                "error", "Incomplete Profile",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Application Failed",
                "message", e.getMessage()
            ));
        }
    }

    // ==================== MY APPLICATIONS ====================
    
    @GetMapping("/my-internship-applications")
    public ResponseEntity<List<InternshipApplicationDTO>> getMyInternshipApplications(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        StudentProfileDTO profile = studentProfileService.getProfileByStudentId(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
        List<InternshipApplicationDTO> applications = internshipApplicationService.getApplicationsByStudentProfile(profile.getProfileId());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/my-placement-applications")
    public ResponseEntity<List<PlacementApplicationDTO>> getMyPlacementApplications(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        List<PlacementApplicationDTO> applications = placementApplicationService.getApplicationsByStudent(student.getStudentId());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/my-consultancy-applications")
    public ResponseEntity<List<ConsultancyProjectApplicationDTO>> getMyConsultancyApplications(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        List<ConsultancyProjectApplicationDTO> applications = consultancyProjectApplicationService.getApplicationsByStudent(student.getStudentId());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/my-applications/summary")
    public ResponseEntity<?> getMyApplicationsSummary(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        StudentProfileDTO profile = studentProfileService.getProfileByStudentId(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
        
        List<InternshipApplicationDTO> internshipApps = internshipApplicationService.getApplicationsByStudentProfile(profile.getProfileId());
        List<PlacementApplicationDTO> placementApps = placementApplicationService.getApplicationsByStudent(student.getStudentId());
        List<ConsultancyProjectApplicationDTO> consultancyApps = consultancyProjectApplicationService.getApplicationsByStudent(student.getStudentId());
        
        // Count applications by status
        long totalApplications = internshipApps.size() + placementApps.size() + consultancyApps.size();
        long appliedCount = internshipApps.stream().filter(app -> app.getStatus() == ApplicationStatus.APPLIED).count() +
                           placementApps.stream().filter(app -> app.getStatus() == ApplicationStatus.APPLIED).count() +
                           consultancyApps.stream().filter(app -> app.getStatus() == ApplicationStatus.APPLIED).count();
        long selectedCount = internshipApps.stream().filter(app -> app.getStatus() == ApplicationStatus.SELECTED).count() +
                            placementApps.stream().filter(app -> app.getStatus() == ApplicationStatus.SELECTED).count() +
                            consultancyApps.stream().filter(app -> app.getStatus() == ApplicationStatus.SELECTED).count();
        long rejectedCount = internshipApps.stream().filter(app -> app.getStatus() == ApplicationStatus.REJECTED).count() +
                            placementApps.stream().filter(app -> app.getStatus() == ApplicationStatus.REJECTED).count() +
                            consultancyApps.stream().filter(app -> app.getStatus() == ApplicationStatus.REJECTED).count();
        
        return ResponseEntity.ok(Map.of(
            "totalApplications", totalApplications,
            "appliedCount", appliedCount,
            "selectedCount", selectedCount,
            "rejectedCount", rejectedCount,
            "internshipApplications", internshipApps,
            "placementApplications", placementApps,
            "consultancyApplications", consultancyApps
        ));
    }

    @GetMapping("/my-internship-applications/{id}")
    public ResponseEntity<InternshipApplicationDTO> getMyInternshipApplicationById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        InternshipApplicationDTO application = internshipApplicationService.getApplicationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        // Verify the application belongs to the current student
        if (!application.getStudentId().equals(student.getStudentId())) {
            throw new UnauthorizedException("You can only view your own applications");
        }
        
        return ResponseEntity.ok(application);
    }

    @GetMapping("/my-placement-applications/{id}")
    public ResponseEntity<PlacementApplicationDTO> getMyPlacementApplicationById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        PlacementApplicationDTO application = placementApplicationService.getApplicationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        // Verify the application belongs to the current student
        if (!application.getStudentId().equals(student.getStudentId())) {
            throw new UnauthorizedException("You can only view your own applications");
        }
        
        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/my-internship-applications/{id}/withdraw")
    public ResponseEntity<Void> withdrawInternshipApplication(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        // Verify the application belongs to the current student
        InternshipApplicationDTO application = internshipApplicationService.getApplicationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        if (!application.getStudentId().equals(student.getStudentId())) {
            throw new UnauthorizedException("You can only withdraw your own applications");
        }
        
        internshipApplicationService.withdrawApplication(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/my-placement-applications/{id}/withdraw")
    public ResponseEntity<Void> withdrawPlacementApplication(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        // Verify the application belongs to the current student
        PlacementApplicationDTO application = placementApplicationService.getApplicationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        if (!application.getStudentId().equals(student.getStudentId())) {
            throw new UnauthorizedException("You can only withdraw your own applications");
        }
        
        placementApplicationService.withdrawApplication(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CONSULTANCY PROJECT VIEWING & APPLICATION ====================
    
    @GetMapping("/consultancy-projects")
    public ResponseEntity<List<ConsultancyProjectWorkDTO>> getAllConsultancyProjects() {
        List<ConsultancyProjectWorkDTO> projects = consultancyProjectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/consultancy-projects/active")
    public ResponseEntity<List<ConsultancyProjectWorkDTO>> getActiveConsultancyProjects() {
        List<ConsultancyProjectWorkDTO> activeProjects = consultancyProjectService.getActiveProjects();
        return ResponseEntity.ok(activeProjects);
    }

    @GetMapping("/consultancy-projects/{id}")
    public ResponseEntity<ConsultancyProjectWorkDTO> getConsultancyProjectById(@PathVariable Long id) {
        return consultancyProjectService.getProjectById(id)
                .map(project -> ResponseEntity.ok(project))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/consultancy-projects/{id}/apply")
    public ResponseEntity<ConsultancyProjectApplicationDTO> applyForConsultancyProject(
            @PathVariable Long id,
            @RequestBody ConsultancyProjectApplicationDTO applicationDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        // Set the student ID and project ID
        applicationDTO.setStudentId(student.getStudentId());
        applicationDTO.setProjectId(id);
        
        // Check if student has already applied
        if (consultancyProjectApplicationService.hasStudentAppliedForProject(student.getStudentId(), id)) {
            return ResponseEntity.badRequest().build();
        }
        
        ConsultancyProjectApplicationDTO createdApplication = consultancyProjectApplicationService.applyForProject(applicationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
    }

    // ==================== GUEST LECTURE VIEWING ====================
    
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

    // ==================== TRAINING WORKSHOP VIEWING ====================
    
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

    // ==================== INDUSTRIAL VISIT VIEWING ====================
    
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

    // ==================== STUDENT PROFILE MANAGEMENT ====================
    
    @GetMapping("/my-profile")
    public ResponseEntity<StudentProfileDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        
        return studentProfileService.getProfileByStudentId(student.getStudentId())
                .map(profile -> ResponseEntity.ok(profile))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/my-profile")
    public ResponseEntity<StudentProfileDTO> createMyProfile(
            @RequestBody StudentProfileDTO profileDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        profileDTO.setStudentId(student.getStudentId());
        
        // Check if profile already exists
        if (studentProfileService.getProfileByStudentId(student.getStudentId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        
        StudentProfileDTO createdProfile = studentProfileService.createProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    @PutMapping("/my-profile")
    public ResponseEntity<StudentProfileDTO> updateMyProfile(
            @RequestBody StudentProfileDTO profileDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        // Find existing profile
        StudentProfileDTO existingProfile = studentProfileService.getProfileByStudentId(student.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
        
        // Ensure the student ID matches
        profileDTO.setStudentId(student.getStudentId());
        
        StudentProfileDTO updatedProfile = studentProfileService.updateProfile(existingProfile.getProfileId(), profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/my-profile/validation")
    public ResponseEntity<?> validateMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        
        List<String> missingFields = applicationFormService.validateStudentProfileForApplication(student.getStudentId());
        boolean isComplete = missingFields.isEmpty();
        
        return ResponseEntity.ok(Map.of(
            "profileComplete", isComplete,
            "missingFields", missingFields,
            "totalMissingFields", missingFields.size(),
            "canApplyForOpportunities", isComplete
        ));
    }

    // ==================== RESUME MANAGEMENT ====================
    
    @PostMapping("/my-profile/resume")
    public ResponseEntity<?> uploadResume(
            @RequestParam("resume") MultipartFile resumeFile,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Student student = getCurrentStudent(userDetails);
        
        try {
            String resumeUrl = resumeService.uploadResume(student.getStudentId(), resumeFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Resume uploaded successfully",
                "resumeUrl", resumeUrl
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Invalid file",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Upload failed",
                "message", "Failed to upload resume. Please try again."
            ));
        }
    }

    @GetMapping("/my-profile/resume")
    public ResponseEntity<InputStreamResource> downloadMyResume(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        
        try {
            var resumeStream = resumeService.downloadResume(student.getStudentId());
            var resumeInfo = resumeService.getResumeFileInfo(student.getStudentId());
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resumeInfo.fileName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, resumeInfo.contentType());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(resumeStream));
                    
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my-profile/resume/info")
    public ResponseEntity<?> getMyResumeInfo(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        
        try {
            var resumeInfo = resumeService.getResumeFileInfo(student.getStudentId());
            return ResponseEntity.ok(resumeInfo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(Map.of(
                "hasResume", false,
                "message", "No resume uploaded"
            ));
        }
    }

    @DeleteMapping("/my-profile/resume")
    public ResponseEntity<?> deleteMyResume(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        
        try {
            resumeService.deleteResume(student.getStudentId());
            return ResponseEntity.ok(Map.of(
                "message", "Resume deleted successfully"
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my-profile/resume/status")
    public ResponseEntity<?> getResumeStatus(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        
        boolean hasResume = resumeService.hasResume(student.getStudentId());
        return ResponseEntity.ok(Map.of(
            "hasResume", hasResume,
            "studentId", student.getStudentId()
        ));
    }
}