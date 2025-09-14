package in.edu.jspmjscoe.admissionportal.services.impl.subject;

import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.repositories.subject.DepartmentRepository;
import in.edu.jspmjscoe.admissionportal.services.subject.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department updateDepartment(Long id, Department updatedDepartment) {
        return departmentRepository.findById(id).map(department -> {
            department.setName(updatedDepartment.getName());
            department.setCode(updatedDepartment.getCode());
            return departmentRepository.save(department);
        }).orElseThrow(() -> new RuntimeException("Department not found with id " + id));
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id " + id);
        }
        departmentRepository.deleteById(id);
    }
}
