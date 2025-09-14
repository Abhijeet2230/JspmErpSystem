package in.edu.jspmjscoe.admissionportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import in.edu.jspmjscoe.admissionportal.model.TeacherAddress;

public interface TeacherAddressRepository extends JpaRepository<TeacherAddress, Long> {
}
