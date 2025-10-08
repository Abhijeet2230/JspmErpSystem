package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.Placement;
import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlacementRepository extends JpaRepository<Placement, Long> {

    List<Placement> findByCompanyCompanyId(Long companyId);

    List<Placement> findByStatus(PostingStatus status);

    List<Placement> findByJobTitleContainingIgnoreCase(String jobTitle);
}


