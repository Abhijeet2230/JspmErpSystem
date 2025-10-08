package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementDTO;
import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementWithEffectiveValuesDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import java.util.List;
import java.util.Optional;

public interface PlacementService {
    
    PlacementDTO createPlacement(PlacementDTO placementDTO);
    
    PlacementDTO updatePlacement(Long placementId, PlacementDTO placementDTO);
    
    Optional<PlacementDTO> getPlacementById(Long placementId);
    
    List<PlacementDTO> getAllPlacements();
    
    List<PlacementDTO> getPlacementsByCompany(Long companyId);
    
    List<PlacementDTO> getPlacementsByStatus(PostingStatus status);
    
    List<PlacementDTO> getPlacementsByJobTitle(String jobTitle);
    
    List<PlacementDTO> getActivePlacements();
    
    void deletePlacement(Long placementId);
    
    void updatePlacementStatus(Long placementId, PostingStatus status);
    
    // Method to get placement with effective values
    Optional<PlacementWithEffectiveValuesDTO> getPlacementWithEffectiveValues(Long placementId);
    
    List<PlacementWithEffectiveValuesDTO> getAllPlacementsWithEffectiveValues();
}
