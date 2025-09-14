package in.edu.jspmjscoe.admissionportal.repositories.student;

import in.edu.jspmjscoe.admissionportal.model.student.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
}
