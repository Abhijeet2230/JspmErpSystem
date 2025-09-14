package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.model.Department;
import org.springframework.stereotype.Service;

@Service
public interface DepartmentService {
    Department saveDepartment(Department department);

    Department getDepartmentById(Long id);
}
