package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipPostingDTO;
import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipPostingWithEffectiveValuesDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.InternshipPostingMapper;
import in.edu.jspmjscoe.admissionportal.mappers.internship.CompanyMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.InternshipPosting;
import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import in.edu.jspmjscoe.admissionportal.repositories.internship.InternshipPostingRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.InternshipPostingService;
import in.edu.jspmjscoe.admissionportal.services.internship.EffectiveValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InternshipPostingServiceImpl implements InternshipPostingService {

    private final InternshipPostingRepository postingRepository;
    private final InternshipPostingMapper postingMapper;
    private final CompanyMapper companyMapper;
    private final EffectiveValueService effectiveValueService;

    @Override
    public InternshipPostingDTO createPosting(InternshipPostingDTO postingDTO) {
        InternshipPosting posting = postingMapper.toEntity(postingDTO);
        InternshipPosting savedPosting = postingRepository.save(posting);
        return postingMapper.toDTO(savedPosting);
    }

    @Override
    public InternshipPostingDTO updatePosting(Long postingId, InternshipPostingDTO postingDTO) {
        InternshipPosting existingPosting = postingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship posting not found with id: " + postingId));
        
        // Use mapper to update entity from DTO
        postingMapper.updateEntityFromDto(postingDTO, existingPosting);
        
        InternshipPosting updatedPosting = postingRepository.save(existingPosting);
        return postingMapper.toDTO(updatedPosting);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InternshipPostingDTO> getPostingById(Long postingId) {
        return postingRepository.findById(postingId)
                .map(postingMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipPostingDTO> getAllPostings() {
        return postingRepository.findAll()
                .stream()
                .map(postingMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipPostingDTO> getPostingsByCompany(Long companyId) {
        return postingRepository.findByCompanyCompanyId(companyId)
                .stream()
                .map(postingMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipPostingDTO> getPostingsByStatus(PostingStatus status) {
        return postingRepository.findByStatus(status)
                .stream()
                .map(postingMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipPostingDTO> getPostingsByTitle(String title) {
        return postingRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(postingMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipPostingDTO> getActivePostings() {
        return postingRepository.findByStatus(PostingStatus.ACTIVE)
                .stream()
                .map(postingMapper::toDTO)
                .toList();
    }

    @Override
    public void deletePosting(Long postingId) {
        if (!postingRepository.existsById(postingId)) {
            throw new ResourceNotFoundException("Internship posting not found with id: " + postingId);
        }
        postingRepository.deleteById(postingId);
    }

    @Override
    public void updatePostingStatus(Long postingId, PostingStatus status) {
        InternshipPosting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship posting not found with id: " + postingId));
        posting.setStatus(status);
        postingRepository.save(posting);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<InternshipPostingWithEffectiveValuesDTO> getPostingWithEffectiveValues(Long postingId) {
        return postingRepository.findById(postingId)
                .map(posting -> {
                    InternshipPostingDTO postingDTO = postingMapper.toDTO(posting);
                    var companyDTO = companyMapper.toDTO(posting.getCompany());
                    
                    return InternshipPostingWithEffectiveValuesDTO.builder()
                            .internshipId(postingDTO.getInternshipId())
                            .companyId(postingDTO.getCompanyId())
                            .companyName(postingDTO.getCompanyName())
                            .title(postingDTO.getTitle())
                            .description(postingDTO.getDescription())
                            .location(postingDTO.getLocation())
                            .startDate(postingDTO.getStartDate())
                            .endDate(postingDTO.getEndDate())
                            .stipend(postingDTO.getStipend())
                            .vacancies(postingDTO.getVacancies())
                            .status(postingDTO.getStatus())
                            .effectiveVacancies(effectiveValueService.getEffectiveVacancies(postingDTO, companyDTO))
                            .effectiveLocation(effectiveValueService.getEffectiveLocation(postingDTO, companyDTO))
                            .company(companyDTO)
                            .build();
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InternshipPostingWithEffectiveValuesDTO> getAllPostingsWithEffectiveValues() {
        return postingRepository.findAll()
                .stream()
                .map(posting -> {
                    InternshipPostingDTO postingDTO = postingMapper.toDTO(posting);
                    var companyDTO = companyMapper.toDTO(posting.getCompany());
                    
                    return InternshipPostingWithEffectiveValuesDTO.builder()
                            .internshipId(postingDTO.getInternshipId())
                            .companyId(postingDTO.getCompanyId())
                            .companyName(postingDTO.getCompanyName())
                            .title(postingDTO.getTitle())
                            .description(postingDTO.getDescription())
                            .location(postingDTO.getLocation())
                            .startDate(postingDTO.getStartDate())
                            .endDate(postingDTO.getEndDate())
                            .stipend(postingDTO.getStipend())
                            .vacancies(postingDTO.getVacancies())
                            .status(postingDTO.getStatus())
                            .effectiveVacancies(effectiveValueService.getEffectiveVacancies(postingDTO, companyDTO))
                            .effectiveLocation(effectiveValueService.getEffectiveLocation(postingDTO, companyDTO))
                            .company(companyDTO)
                            .build();
                })
                .toList();
    }
}
