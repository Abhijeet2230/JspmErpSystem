package in.edu.jspmjscoe.admissionportal.controllers.admin.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.*;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import in.edu.jspmjscoe.admissionportal.model.internship.ProjectStatus;
import in.edu.jspmjscoe.admissionportal.services.internship.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin Controller for Internship Management
 * 
 * Admin Role Permissions:
 * - Full CRUD operations on all internship-related entities
 * - Can manage companies, internships, placements, applications
 * - Can view all data across the system
 * - Can update statuses and manage workflows
 */
@RestController
@RequestMapping("/api/admin/internships")
@RequiredArgsConstructor
public class AdminInternshipController {

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
    private final StudentInternshipProfileService studentInternshipProfileService;
    private final ResumeService resumeService;

    // ==================== COMPANY MANAGEMENT ====================
    
    @PostMapping("/companies")
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO createdCompany = companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

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

    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyDTO companyDTO) {
        CompanyDTO updatedCompany = companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/companies/search")
    public ResponseEntity<List<CompanyDTO>> searchCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String hiringStatus,
            @RequestParam(required = false) Integer minVacancies) {
        
        if (name != null) {
            return ResponseEntity.ok(companyService.getCompaniesByName(name));
        } else if (industry != null) {
            return ResponseEntity.ok(companyService.getCompaniesByIndustry(industry));
        } else if (hiringStatus != null) {
            return ResponseEntity.ok(companyService.getCompaniesByHiringStatus(hiringStatus));
        } else if (minVacancies != null) {
            return ResponseEntity.ok(companyService.getCompaniesWithDefaultVacancies(minVacancies));
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

    @PutMapping("/postings/{id}")
    public ResponseEntity<InternshipPostingDTO> updateInternshipPosting(@PathVariable Long id, @RequestBody InternshipPostingDTO postingDTO) {
        InternshipPostingDTO updatedPosting = internshipPostingService.updatePosting(id, postingDTO);
        return ResponseEntity.ok(updatedPosting);
    }

    @PutMapping("/postings/{id}/status")
    public ResponseEntity<Void> updateInternshipPostingStatus(@PathVariable Long id, @RequestParam PostingStatus status) {
        internshipPostingService.updatePostingStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/postings/{id}")
    public ResponseEntity<Void> deleteInternshipPosting(@PathVariable Long id) {
        internshipPostingService.deletePosting(id);
        return ResponseEntity.noContent().build();
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

    @PutMapping("/placements/{id}")
    public ResponseEntity<PlacementDTO> updatePlacement(@PathVariable Long id, @RequestBody PlacementDTO placementDTO) {
        PlacementDTO updatedPlacement = placementService.updatePlacement(id, placementDTO);
        return ResponseEntity.ok(updatedPlacement);
    }

    @PutMapping("/placements/{id}/status")
    public ResponseEntity<Void> updatePlacementStatus(@PathVariable Long id, @RequestParam PostingStatus status) {
        placementService.updatePlacementStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/placements/{id}")
    public ResponseEntity<Void> deletePlacement(@PathVariable Long id) {
        placementService.deletePlacement(id);
        return ResponseEntity.noContent().build();
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

    // ==================== APPLICATION MANAGEMENT ====================
    
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

    @PutMapping("/internship-applications/{id}/status")
    public ResponseEntity<Void> updateInternshipApplicationStatus(@PathVariable Long id, @RequestParam ApplicationStatus status) {
        internshipApplicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/internship-applications/{id}")
    public ResponseEntity<Void> deleteInternshipApplication(@PathVariable Long id) {
        internshipApplicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
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

    @PutMapping("/placement-applications/{id}/status")
    public ResponseEntity<Void> updatePlacementApplicationStatus(@PathVariable Long id, @RequestParam ApplicationStatus status) {
        placementApplicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/placement-applications/{id}")
    public ResponseEntity<Void> deletePlacementApplication(@PathVariable Long id) {
        placementApplicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CONSULTANCY PROJECT MANAGEMENT ====================
    
    @PostMapping("/consultancy-projects")
    public ResponseEntity<ConsultancyProjectWorkDTO> createConsultancyProject(@RequestBody ConsultancyProjectWorkDTO projectDTO) {
        ConsultancyProjectWorkDTO createdProject = consultancyProjectService.createProject(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

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

    @PutMapping("/consultancy-projects/{id}")
    public ResponseEntity<ConsultancyProjectWorkDTO> updateConsultancyProject(@PathVariable Long id, @RequestBody ConsultancyProjectWorkDTO projectDTO) {
        ConsultancyProjectWorkDTO updatedProject = consultancyProjectService.updateProject(id, projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @PutMapping("/consultancy-projects/{id}/status")
    public ResponseEntity<Void> updateConsultancyProjectStatus(@PathVariable Long id, @RequestParam ProjectStatus status) {
        consultancyProjectService.updateProjectStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/consultancy-projects/{id}")
    public ResponseEntity<Void> deleteConsultancyProject(@PathVariable Long id) {
        consultancyProjectService.deleteProject(id);
        return ResponseEntity.noContent().build();
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

    @PutMapping("/guest-lectures/{id}")
    public ResponseEntity<GuestLectureDTO> updateGuestLecture(@PathVariable Long id, @RequestBody GuestLectureDTO guestLectureDTO) {
        GuestLectureDTO updatedLecture = guestLectureService.updateGuestLecture(id, guestLectureDTO);
        return ResponseEntity.ok(updatedLecture);
    }

    @DeleteMapping("/guest-lectures/{id}")
    public ResponseEntity<Void> deleteGuestLecture(@PathVariable Long id) {
        guestLectureService.deleteGuestLecture(id);
        return ResponseEntity.noContent().build();
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

    @PutMapping("/training-workshops/{id}")
    public ResponseEntity<TrainingSkillWorkshopDTO> updateTrainingWorkshop(@PathVariable Long id, @RequestBody TrainingSkillWorkshopDTO workshopDTO) {
        TrainingSkillWorkshopDTO updatedWorkshop = trainingSkillWorkshopService.updateWorkshop(id, workshopDTO);
        return ResponseEntity.ok(updatedWorkshop);
    }

    @DeleteMapping("/training-workshops/{id}")
    public ResponseEntity<Void> deleteTrainingWorkshop(@PathVariable Long id) {
        trainingSkillWorkshopService.deleteWorkshop(id);
        return ResponseEntity.noContent().build();
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

    @PutMapping("/industrial-visits/{id}")
    public ResponseEntity<IndustrialVisitDTO> updateIndustrialVisit(@PathVariable Long id, @RequestBody IndustrialVisitDTO industrialVisitDTO) {
        IndustrialVisitDTO updatedVisit = industrialVisitService.updateIndustrialVisit(id, industrialVisitDTO);
        return ResponseEntity.ok(updatedVisit);
    }

    @DeleteMapping("/industrial-visits/{id}")
    public ResponseEntity<Void> deleteIndustrialVisit(@PathVariable Long id) {
        industrialVisitService.deleteIndustrialVisit(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== STUDENT PROFILE MANAGEMENT ====================
    
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

    @PutMapping("/student-profiles/{id}")
    public ResponseEntity<StudentProfileDTO> updateStudentProfile(@PathVariable Long id, @RequestBody StudentProfileDTO profileDTO) {
        StudentProfileDTO updatedProfile = studentInternshipProfileService.updateProfile(id, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/student-profiles/{id}")
    public ResponseEntity<Void> deleteStudentProfile(@PathVariable Long id) {
        studentInternshipProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== RESUME MANAGEMENT FOR ADMIN ====================
    
    @GetMapping("/student-profiles/{studentId}/resume")
    public ResponseEntity<InputStreamResource> downloadStudentResume(@PathVariable Long studentId) {
        try {
            var resumeStream = resumeService.downloadResume(studentId);
            var resumeInfo = resumeService.getResumeFileInfo(studentId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"student_" + studentId + "_" + resumeInfo.fileName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, resumeInfo.contentType());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(resumeStream));
                    
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

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

    @DeleteMapping("/student-profiles/{studentId}/resume")
    public ResponseEntity<?> deleteStudentResume(@PathVariable Long studentId) {
        try {
            resumeService.deleteResume(studentId);
            return ResponseEntity.ok(Map.of(
                "message", "Student resume deleted successfully",
                "studentId", studentId
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}