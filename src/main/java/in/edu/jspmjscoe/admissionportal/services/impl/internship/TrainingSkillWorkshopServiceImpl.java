package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.TrainingSkillWorkshopDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.TrainingSkillWorkshopMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.TrainingSkillWorkshop;
import in.edu.jspmjscoe.admissionportal.repositories.internship.TrainingSkillWorkshopRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.TrainingSkillWorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainingSkillWorkshopServiceImpl implements TrainingSkillWorkshopService {

    private final TrainingSkillWorkshopRepository trainingSkillWorkshopRepository;
    private final TrainingSkillWorkshopMapper trainingSkillWorkshopMapper;

    @Override
    public TrainingSkillWorkshopDTO createWorkshop(TrainingSkillWorkshopDTO workshopDTO) {
        TrainingSkillWorkshop workshop = trainingSkillWorkshopMapper.toEntity(workshopDTO);
        TrainingSkillWorkshop savedWorkshop = trainingSkillWorkshopRepository.save(workshop);
        return trainingSkillWorkshopMapper.toDTO(savedWorkshop);
    }

    @Override
    public TrainingSkillWorkshopDTO updateWorkshop(Long workshopId, TrainingSkillWorkshopDTO workshopDTO) {
        TrainingSkillWorkshop existingWorkshop = trainingSkillWorkshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Training workshop not found with id: " + workshopId));
        
        // Use mapper to update entity from DTO
        trainingSkillWorkshopMapper.updateEntityFromDto(workshopDTO, existingWorkshop);
        
        TrainingSkillWorkshop updatedWorkshop = trainingSkillWorkshopRepository.save(existingWorkshop);
        return trainingSkillWorkshopMapper.toDTO(updatedWorkshop);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingSkillWorkshopDTO> getWorkshopById(Long workshopId) {
        return trainingSkillWorkshopRepository.findById(workshopId)
                .map(trainingSkillWorkshopMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingSkillWorkshopDTO> getAllWorkshops() {
        return trainingSkillWorkshopRepository.findAll()
                .stream()
                .map(trainingSkillWorkshopMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingSkillWorkshopDTO> getWorkshopsByTitle(String title) {
        return trainingSkillWorkshopRepository.findByWorkshopTitleContainingIgnoreCase(title)
                .stream()
                .map(trainingSkillWorkshopMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingSkillWorkshopDTO> getWorkshopsByOrganization(String organization) {
        return trainingSkillWorkshopRepository.findByOrganizationContainingIgnoreCase(organization)
                .stream()
                .map(trainingSkillWorkshopMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingSkillWorkshopDTO> getWorkshopsByDate(LocalDate workshopDate) {
        return trainingSkillWorkshopRepository.findByWorkshopDate(workshopDate)
                .stream()
                .map(trainingSkillWorkshopMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteWorkshop(Long workshopId) {
        if (!trainingSkillWorkshopRepository.existsById(workshopId)) {
            throw new ResourceNotFoundException("Training workshop not found with id: " + workshopId);
        }
        trainingSkillWorkshopRepository.deleteById(workshopId);
    }
}