package in.edu.jspmjscoe.admissionportal.repositories.assessment;

import in.edu.jspmjscoe.admissionportal.model.assessment.AssessmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentTypeRepository extends JpaRepository<AssessmentType, Long> {
}
