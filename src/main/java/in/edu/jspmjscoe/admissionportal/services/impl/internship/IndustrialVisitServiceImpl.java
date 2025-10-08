package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.IndustrialVisitDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.IndustrialVisitMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.IndustrialVisit;
import in.edu.jspmjscoe.admissionportal.repositories.internship.IndustrialVisitRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.IndustrialVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class IndustrialVisitServiceImpl implements IndustrialVisitService {

    private final IndustrialVisitRepository industrialVisitRepository;
    private final IndustrialVisitMapper industrialVisitMapper;

    @Override
    public IndustrialVisitDTO createIndustrialVisit(IndustrialVisitDTO industrialVisitDTO) {
        IndustrialVisit industrialVisit = industrialVisitMapper.toEntity(industrialVisitDTO);
        IndustrialVisit savedIndustrialVisit = industrialVisitRepository.save(industrialVisit);
        return industrialVisitMapper.toDTO(savedIndustrialVisit);
    }

    @Override
    public IndustrialVisitDTO updateIndustrialVisit(Long visitId, IndustrialVisitDTO industrialVisitDTO) {
        IndustrialVisit existingIndustrialVisit = industrialVisitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Industrial visit not found with id: " + visitId));
        
        // Use mapper to update entity from DTO
        industrialVisitMapper.updateEntityFromDto(industrialVisitDTO, existingIndustrialVisit);
        
        IndustrialVisit updatedIndustrialVisit = industrialVisitRepository.save(existingIndustrialVisit);
        return industrialVisitMapper.toDTO(updatedIndustrialVisit);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IndustrialVisitDTO> getIndustrialVisitById(Long visitId) {
        return industrialVisitRepository.findById(visitId)
                .map(industrialVisitMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IndustrialVisitDTO> getAllIndustrialVisits() {
        return industrialVisitRepository.findAll()
                .stream()
                .map(industrialVisitMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IndustrialVisitDTO> getIndustrialVisitsByTitle(String title) {
        return industrialVisitRepository.findByVisitTitleContainingIgnoreCase(title)
                .stream()
                .map(industrialVisitMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IndustrialVisitDTO> getIndustrialVisitsByOrganization(String organization) {
        return industrialVisitRepository.findByOrganizationContainingIgnoreCase(organization)
                .stream()
                .map(industrialVisitMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IndustrialVisitDTO> getIndustrialVisitsByDate(LocalDate visitDate) {
        return industrialVisitRepository.findByVisitDate(visitDate)
                .stream()
                .map(industrialVisitMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteIndustrialVisit(Long visitId) {
        if (!industrialVisitRepository.existsById(visitId)) {
            throw new ResourceNotFoundException("Industrial visit not found with id: " + visitId);
        }
        industrialVisitRepository.deleteById(visitId);
    }
}