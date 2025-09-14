package in.edu.jspmjscoe.admissionportal.repositories.subject;

import in.edu.jspmjscoe.admissionportal.model.subject.ElectiveGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectiveGroupRepository extends JpaRepository<ElectiveGroup, Long> {
}
