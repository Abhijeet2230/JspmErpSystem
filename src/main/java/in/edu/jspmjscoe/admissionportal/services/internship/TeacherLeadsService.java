package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.*;

import java.util.List;

public interface TeacherLeadsService {

    List<InternshipPostingDTO> getMyInternshipLeads(String username);

    List<PlacementDTO> getMyPlacementLeads(String username);

    List<ConsultancyProjectWorkDTO> getMyConsultancyProjectLeads(String username);

    List<GuestLectureDTO> getMyGuestLectureLeads(String username);

    List<IndustrialVisitDTO> getMyIndustrialVisitLeads(String username);

    List<TrainingSkillWorkshopDTO> getMyTrainingWorkshopLeads(String username);

    LeadsSummaryDTO getMyLeadsSummary(String username);
}
