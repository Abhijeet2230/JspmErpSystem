package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.InternshipPosting;
import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipPostingRepository extends JpaRepository<InternshipPosting, Long> {

    List<InternshipPosting> findByCompanyCompanyId(Long companyId);

    List<InternshipPosting> findByStatus(PostingStatus status);

    List<InternshipPosting> findByTitleContainingIgnoreCase(String title);
    
    List<InternshipPosting> findByReferredBy_UserId(Long userId);
}


