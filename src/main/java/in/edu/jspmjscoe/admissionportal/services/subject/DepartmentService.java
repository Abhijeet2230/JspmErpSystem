package in.edu.jspmjscoe.admissionportal.services.subject;


import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {
    Department saveDepartment(Department department);

    Department getDepartmentById(Long id);

    List<Department> getAllDepartments();

    Department updateDepartment(Long id, Department updatedDepartment);

    void deleteDepartment(Long id);
}
