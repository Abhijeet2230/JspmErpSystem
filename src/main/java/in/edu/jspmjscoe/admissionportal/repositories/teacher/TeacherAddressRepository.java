package in.edu.jspmjscoe.admissionportal.repositories.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherAddress;

public interface TeacherAddressRepository extends JpaRepository<TeacherAddress, Long> {
}
