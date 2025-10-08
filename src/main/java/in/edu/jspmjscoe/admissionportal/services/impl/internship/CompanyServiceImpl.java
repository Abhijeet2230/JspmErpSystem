package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.CompanyDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.CompanyMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.Company;
import in.edu.jspmjscoe.admissionportal.repositories.internship.CompanyRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = companyMapper.toEntity(companyDTO);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toDTO(savedCompany);
    }

    @Override
    public CompanyDTO updateCompany(Long companyId, CompanyDTO companyDTO) {
        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        
        // Use mapper to update entity from DTO
        companyMapper.updateEntityFromDto(companyDTO, existingCompany);
        
        Company updatedCompany = companyRepository.save(existingCompany);
        return companyMapper.toDTO(updatedCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .map(companyMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(companyMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompaniesByName(String name) {
        return companyRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(companyMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompaniesByIndustry(String industryType) {
        return companyRepository.findByIndustryType(industryType)
                .stream()
                .map(companyMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompaniesByHiringStatus(String hiringStatus) {
        return companyRepository.findByHiringStatus(hiringStatus)
                .stream()
                .map(companyMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompaniesWithDefaultVacancies(Integer minVacancies) {
        return companyRepository.findByDefaultVacanciesGreaterThan(minVacancies)
                .stream()
                .map(companyMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteCompany(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company not found with id: " + companyId);
        }
        companyRepository.deleteById(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndYearEstablished(String name, Integer yearEstablished) {
        return companyRepository.existsByNameAndYearEstablished(name, yearEstablished);
    }
}
