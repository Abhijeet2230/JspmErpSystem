package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.*;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.*;
import in.edu.jspmjscoe.admissionportal.model.internship.*;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.repositories.internship.*;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.TeacherLeadsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherLeadsServiceImpl implements TeacherLeadsService {

    private final UserRepository userRepository;
    private final InternshipPostingRepository internshipPostingRepository;
    private final PlacementRepository placementRepository;
    private final ConsultancyProjectWorkRepository consultancyProjectWorkRepository;
    private final GuestLectureRepository guestLectureRepository;
    private final IndustrialVisitRepository industrialVisitRepository;
    private final TrainingSkillWorkshopRepository trainingSkillWorkshopRepository;

    private final InternshipPostingMapper internshipPostingMapper;
    private final PlacementMapper placementMapper;
    private final ConsultancyProjectWorkMapper consultancyProjectWorkMapper;
    private final GuestLectureMapper guestLectureMapper;
    private final IndustrialVisitMapper industrialVisitMapper;
    private final TrainingSkillWorkshopMapper trainingSkillWorkshopMapper;

    @Override
    public List<InternshipPostingDTO> getMyInternshipLeads(String username) {
        User user = getUserByUsername(username);
        List<InternshipPosting> leads = internshipPostingRepository.findByReferredBy_UserId(user.getUserId());
        return leads.stream()
                .map(internshipPostingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlacementDTO> getMyPlacementLeads(String username) {
        User user = getUserByUsername(username);
        List<Placement> leads = placementRepository.findByReferredBy_UserId(user.getUserId());
        return leads.stream()
                .map(placementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultancyProjectWorkDTO> getMyConsultancyProjectLeads(String username) {
        User user = getUserByUsername(username);
        List<ConsultancyProjectWork> leads = consultancyProjectWorkRepository.findByReferredBy_UserId(user.getUserId());
        return leads.stream()
                .map(consultancyProjectWorkMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GuestLectureDTO> getMyGuestLectureLeads(String username) {
        User user = getUserByUsername(username);
        List<GuestLecture> leads = guestLectureRepository.findByReferredBy_UserId(user.getUserId());
        return leads.stream()
                .map(guestLectureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IndustrialVisitDTO> getMyIndustrialVisitLeads(String username) {
        User user = getUserByUsername(username);
        List<IndustrialVisit> leads = industrialVisitRepository.findByReferredBy_UserId(user.getUserId());
        return leads.stream()
                .map(industrialVisitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingSkillWorkshopDTO> getMyTrainingWorkshopLeads(String username) {
        User user = getUserByUsername(username);
        List<TrainingSkillWorkshop> leads = trainingSkillWorkshopRepository.findByReferredBy_UserId(user.getUserId());
        return leads.stream()
                .map(trainingSkillWorkshopMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LeadsSummaryDTO getMyLeadsSummary(String username) {
        User user = getUserByUsername(username);
        Long userId = user.getUserId();

        int internships = internshipPostingRepository.findByReferredBy_UserId(userId).size();
        int placements = placementRepository.findByReferredBy_UserId(userId).size();
        int consultancyProjects = consultancyProjectWorkRepository.findByReferredBy_UserId(userId).size();
        int guestLectures = guestLectureRepository.findByReferredBy_UserId(userId).size();
        int industrialVisits = industrialVisitRepository.findByReferredBy_UserId(userId).size();
        int trainingWorkshops = trainingSkillWorkshopRepository.findByReferredBy_UserId(userId).size();

        int totalLeads = internships + placements + consultancyProjects + 
                        guestLectures + industrialVisits + trainingWorkshops;

        String teacherName = getTeacherName(user);

        return LeadsSummaryDTO.builder()
                .teacherId(userId)
                .teacherName(teacherName)
                .totalLeads(totalLeads)
                .internships(internships)
                .placements(placements)
                .consultancyProjects(consultancyProjects)
                .guestLectures(guestLectures)
                .industrialVisits(industrialVisits)
                .trainingWorkshops(trainingWorkshops)
                .build();
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private String getTeacherName(User user) {
        if (user.getTeacher() != null) {
            var teacher = user.getTeacher();
            StringBuilder name = new StringBuilder();
            if (teacher.getPrefix() != null) name.append(teacher.getPrefix()).append(" ");
            if (teacher.getFirstName() != null) name.append(teacher.getFirstName()).append(" ");
            if (teacher.getMiddleName() != null) name.append(teacher.getMiddleName()).append(" ");
            if (teacher.getLastName() != null) name.append(teacher.getLastName());
            String fullName = name.toString().trim();
            return fullName.isEmpty() ? user.getUserName() : fullName;
        }
        return user.getUserName();
    }
}
