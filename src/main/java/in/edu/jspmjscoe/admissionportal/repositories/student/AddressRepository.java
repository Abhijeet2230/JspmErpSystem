package in.edu.jspmjscoe.admissionportal.repositories.student;

import in.edu.jspmjscoe.admissionportal.model.student.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
