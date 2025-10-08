package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementDTO;
import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementWithEffectiveValuesDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.PlacementMapper;
import in.edu.jspmjscoe.admissionportal.mappers.internship.CompanyMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.Placement;
import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import in.edu.jspmjscoe.admissionportal.repositories.internship.PlacementRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.PlacementService;
import in.edu.jspmjscoe.admissionportal.services.internship.EffectiveValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlacementServiceImpl implements PlacementService {

    private final PlacementRepository placementRepository;
    private final PlacementMapper placementMapper;
    private final CompanyMapper companyMapper;
    private final EffectiveValueService effectiveValueService;

    @Override
    public PlacementDTO createPlacement(PlacementDTO placementDTO) {
        Placement placement = placementMapper.toEntity(placementDTO);
        Placement savedPlacement = placementRepository.save(placement);
        return placementMapper.toDTO(savedPlacement);
    }

    @Override
    public PlacementDTO updatePlacement(Long placementId, PlacementDTO placementDTO) {
        Placement existingPlacement = placementRepository.findById(placementId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement not found with id: " + placementId));
        
        // Use mapper to update entity from DTO
        placementMapper.updateEntityFromDto(placementDTO, existingPlacement);
        
        Placement updatedPlacement = placementRepository.save(existingPlacement);
        return placementMapper.toDTO(updatedPlacement);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlacementDTO> getPlacementById(Long placementId) {
        return placementRepository.findById(placementId)
                .map(placementMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementDTO> getAllPlacements() {
        return placementRepository.findAll()
                .stream()
                .map(placementMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementDTO> getPlacementsByCompany(Long companyId) {
        return placementRepository.findByCompanyCompanyId(companyId)
                .stream()
                .map(placementMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementDTO> getPlacementsByStatus(PostingStatus status) {
        return placementRepository.findByStatus(status)
                .stream()
                .map(placementMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementDTO> getPlacementsByJobTitle(String jobTitle) {
        return placementRepository.findByJobTitleContainingIgnoreCase(jobTitle)
                .stream()
                .map(placementMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementDTO> getActivePlacements() {
        return placementRepository.findByStatus(PostingStatus.ACTIVE)
                .stream()
                .map(placementMapper::toDTO)
                .toList();
    }

    @Override
    public void deletePlacement(Long placementId) {
        if (!placementRepository.existsById(placementId)) {
            throw new ResourceNotFoundException("Placement not found with id: " + placementId);
        }
        placementRepository.deleteById(placementId);
    }

    @Override
    public void updatePlacementStatus(Long placementId, PostingStatus status) {
        Placement placement = placementRepository.findById(placementId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement not found with id: " + placementId));
        placement.setStatus(status);
        placementRepository.save(placement);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PlacementWithEffectiveValuesDTO> getPlacementWithEffectiveValues(Long placementId) {
        return placementRepository.findById(placementId)
                .map(placement -> {
                    PlacementDTO placementDTO = placementMapper.toDTO(placement);
                    var companyDTO = companyMapper.toDTO(placement.getCompany());
                    
                    return PlacementWithEffectiveValuesDTO.builder()
                            .placementId(placementDTO.getPlacementId())
                            .companyId(placementDTO.getCompanyId())
                            .companyName(placementDTO.getCompanyName())
                            .jobTitle(placementDTO.getJobTitle())
                            .description(placementDTO.getDescription())
                            .location(placementDTO.getLocation())
                            .startDate(placementDTO.getStartDate())
                            .endDate(placementDTO.getEndDate())
                            .ctc(placementDTO.getCtc())
                            .bondRequired(placementDTO.getBondRequired())
                            .vacancies(placementDTO.getVacancies())
                            .status(placementDTO.getStatus())
                            .effectiveVacancies(effectiveValueService.getEffectiveVacancies(placementDTO, companyDTO))
                            .effectiveCtc(effectiveValueService.getEffectiveCtc(placementDTO, companyDTO))
                            .effectiveBondRequired(effectiveValueService.getEffectiveBondRequired(placementDTO, companyDTO))
                            .effectiveLocation(effectiveValueService.getEffectiveLocation(placementDTO, companyDTO))
                            .company(companyDTO)
                            .build();
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PlacementWithEffectiveValuesDTO> getAllPlacementsWithEffectiveValues() {
        return placementRepository.findAll()
                .stream()
                .map(placement -> {
                    PlacementDTO placementDTO = placementMapper.toDTO(placement);
                    var companyDTO = companyMapper.toDTO(placement.getCompany());
                    
                    return PlacementWithEffectiveValuesDTO.builder()
                            .placementId(placementDTO.getPlacementId())
                            .companyId(placementDTO.getCompanyId())
                            .companyName(placementDTO.getCompanyName())
                            .jobTitle(placementDTO.getJobTitle())
                            .description(placementDTO.getDescription())
                            .location(placementDTO.getLocation())
                            .startDate(placementDTO.getStartDate())
                            .endDate(placementDTO.getEndDate())
                            .ctc(placementDTO.getCtc())
                            .bondRequired(placementDTO.getBondRequired())
                            .vacancies(placementDTO.getVacancies())
                            .status(placementDTO.getStatus())
                            .effectiveVacancies(effectiveValueService.getEffectiveVacancies(placementDTO, companyDTO))
                            .effectiveCtc(effectiveValueService.getEffectiveCtc(placementDTO, companyDTO))
                            .effectiveBondRequired(effectiveValueService.getEffectiveBondRequired(placementDTO, companyDTO))
                            .effectiveLocation(effectiveValueService.getEffectiveLocation(placementDTO, companyDTO))
                            .company(companyDTO)
                            .build();
                })
                .toList();
    }
}
