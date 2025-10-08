package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.TrainingSkillWorkshopDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingSkillWorkshopService {
    
    TrainingSkillWorkshopDTO createWorkshop(TrainingSkillWorkshopDTO workshopDTO);
    
    TrainingSkillWorkshopDTO updateWorkshop(Long workshopId, TrainingSkillWorkshopDTO workshopDTO);
    
    Optional<TrainingSkillWorkshopDTO> getWorkshopById(Long workshopId);
    
    List<TrainingSkillWorkshopDTO> getAllWorkshops();
    
    List<TrainingSkillWorkshopDTO> getWorkshopsByTitle(String title);
    
    List<TrainingSkillWorkshopDTO> getWorkshopsByOrganization(String organization);
    
    List<TrainingSkillWorkshopDTO> getWorkshopsByDate(LocalDate workshopDate);
    
    void deleteWorkshop(Long workshopId);
}