package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import in.edu.jspmjscoe.admissionportal.model.internship.PlacementApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlacementApplicationRepository extends JpaRepository<PlacementApplication, Long> {

    List<PlacementApplication> findByStudentStudentId(Long studentId);

    List<PlacementApplication> findByPlacementPlacementId(Long placementId);

    List<PlacementApplication> findByStatus(ApplicationStatus status);

    boolean existsByStudentStudentIdAndPlacementPlacementId(Long studentId, Long placementId);
}


