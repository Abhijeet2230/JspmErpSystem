package in.edu.jspmjscoe.admissionportal.repositories.subject;

import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

}
