package in.edu.jspmjscoe.admissionportal.repositories;

import in.edu.jspmjscoe.admissionportal.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

}
