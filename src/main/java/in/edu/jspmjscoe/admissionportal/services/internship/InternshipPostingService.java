package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipPostingDTO;
import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipPostingWithEffectiveValuesDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import java.util.List;
import java.util.Optional;

public interface InternshipPostingService {
    
    InternshipPostingDTO createPosting(InternshipPostingDTO postingDTO);
    
    InternshipPostingDTO updatePosting(Long postingId, InternshipPostingDTO postingDTO);
    
    Optional<InternshipPostingDTO> getPostingById(Long postingId);
    
    List<InternshipPostingDTO> getAllPostings();
    
    List<InternshipPostingDTO> getPostingsByCompany(Long companyId);
    
    List<InternshipPostingDTO> getPostingsByStatus(PostingStatus status);
    
    List<InternshipPostingDTO> getPostingsByTitle(String title);
    
    List<InternshipPostingDTO> getActivePostings();
    
    void deletePosting(Long postingId);
    
    void updatePostingStatus(Long postingId, PostingStatus status);
    
    // Method to get posting with effective values
    Optional<InternshipPostingWithEffectiveValuesDTO> getPostingWithEffectiveValues(Long postingId);
    
    List<InternshipPostingWithEffectiveValuesDTO> getAllPostingsWithEffectiveValues();
}
