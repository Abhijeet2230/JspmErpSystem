package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.IndustrialVisitDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IndustrialVisitService {
    
    IndustrialVisitDTO createIndustrialVisit(IndustrialVisitDTO industrialVisitDTO);
    
    IndustrialVisitDTO updateIndustrialVisit(Long visitId, IndustrialVisitDTO industrialVisitDTO);
    
    Optional<IndustrialVisitDTO> getIndustrialVisitById(Long visitId);
    
    List<IndustrialVisitDTO> getAllIndustrialVisits();
    
    List<IndustrialVisitDTO> getIndustrialVisitsByTitle(String title);
    
    List<IndustrialVisitDTO> getIndustrialVisitsByOrganization(String organization);
    
    List<IndustrialVisitDTO> getIndustrialVisitsByDate(LocalDate visitDate);
    
    void deleteIndustrialVisit(Long visitId);
}