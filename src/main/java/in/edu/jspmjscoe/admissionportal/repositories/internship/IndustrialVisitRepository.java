package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.IndustrialVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IndustrialVisitRepository extends JpaRepository<IndustrialVisit, Long> {

    List<IndustrialVisit> findByOrganizationContainingIgnoreCase(String organization);

    List<IndustrialVisit> findByVisitTitleContainingIgnoreCase(String visitTitle);

    List<IndustrialVisit> findByVisitDate(LocalDate visitDate);
    
    List<IndustrialVisit> findByReferredBy_UserId(Long userId);
}


