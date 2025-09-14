package in.edu.jspmjscoe.admissionportal.services.subject;

import in.edu.jspmjscoe.admissionportal.model.subject.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department saveDepartment(Department department);
    Optional<Department> getDepartmentById(Long id);
    List<Department> getAllDepartments();
    Department updateDepartment(Long id, Department updatedDepartment);
    void deleteDepartment(Long id);
}
