package in.edu.jspmjscoe.admissionportal.controllers.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.internship.*;
import in.edu.jspmjscoe.admissionportal.services.internship.TeacherLeadsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher/internships/my-leads")
@RequiredArgsConstructor
public class TeacherLeadsController {

    private final TeacherLeadsService teacherLeadsService;

    @GetMapping("/internships")
    public ResponseEntity<List<InternshipPostingDTO>> getMyInternshipLeads(Authentication authentication) {
        String username = authentication.getName();
        List<InternshipPostingDTO> leads = teacherLeadsService.getMyInternshipLeads(username);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/placements")
    public ResponseEntity<List<PlacementDTO>> getMyPlacementLeads(Authentication authentication) {
        String username = authentication.getName();
        List<PlacementDTO> leads = teacherLeadsService.getMyPlacementLeads(username);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/consultancy-projects")
    public ResponseEntity<List<ConsultancyProjectWorkDTO>> getMyConsultancyProjectLeads(Authentication authentication) {
        String username = authentication.getName();
        List<ConsultancyProjectWorkDTO> leads = teacherLeadsService.getMyConsultancyProjectLeads(username);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/guest-lectures")
    public ResponseEntity<List<GuestLectureDTO>> getMyGuestLectureLeads(Authentication authentication) {
        String username = authentication.getName();
        List<GuestLectureDTO> leads = teacherLeadsService.getMyGuestLectureLeads(username);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/industrial-visits")
    public ResponseEntity<List<IndustrialVisitDTO>> getMyIndustrialVisitLeads(Authentication authentication) {
        String username = authentication.getName();
        List<IndustrialVisitDTO> leads = teacherLeadsService.getMyIndustrialVisitLeads(username);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/training-workshops")
    public ResponseEntity<List<TrainingSkillWorkshopDTO>> getMyTrainingWorkshopLeads(Authentication authentication) {
        String username = authentication.getName();
        List<TrainingSkillWorkshopDTO> leads = teacherLeadsService.getMyTrainingWorkshopLeads(username);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/summary")
    public ResponseEntity<LeadsSummaryDTO> getMyLeadsSummary(Authentication authentication) {
        String username = authentication.getName();
        LeadsSummaryDTO summary = teacherLeadsService.getMyLeadsSummary(username);
        return ResponseEntity.ok(summary);
    }
}
