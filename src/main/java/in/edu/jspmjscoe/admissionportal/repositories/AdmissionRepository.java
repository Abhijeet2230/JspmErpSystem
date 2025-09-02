package in.edu.jspmjscoe.admissionportal.repositories;

import in.edu.jspmjscoe.admissionportal.model.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
}
