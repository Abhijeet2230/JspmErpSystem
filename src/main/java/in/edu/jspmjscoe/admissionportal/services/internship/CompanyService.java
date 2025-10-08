package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.CompanyDTO;
import java.util.List;
import java.util.Optional;

public interface CompanyService {
    
    CompanyDTO createCompany(CompanyDTO companyDTO);
    
    CompanyDTO updateCompany(Long companyId, CompanyDTO companyDTO);
    
    Optional<CompanyDTO> getCompanyById(Long companyId);
    
    List<CompanyDTO> getAllCompanies();
    
    List<CompanyDTO> getCompaniesByName(String name);
    
    List<CompanyDTO> getCompaniesByIndustry(String industryType);
    
    List<CompanyDTO> getCompaniesByHiringStatus(String hiringStatus);
    
    List<CompanyDTO> getCompaniesWithDefaultVacancies(Integer minVacancies);
    
    void deleteCompany(Long companyId);
    
    boolean existsByNameAndYearEstablished(String name, Integer yearEstablished);
}
