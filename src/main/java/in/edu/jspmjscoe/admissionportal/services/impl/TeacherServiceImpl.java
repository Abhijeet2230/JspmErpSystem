package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.mappers.TeacherMapper;
import in.edu.jspmjscoe.admissionportal.model.Department;
import in.edu.jspmjscoe.admissionportal.model.Teacher;
import in.edu.jspmjscoe.admissionportal.model.User;
import in.edu.jspmjscoe.admissionportal.repositories.DepartmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.repositories.UserRepository;
import in.edu.jspmjscoe.admissionportal.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        return TeacherMapper.toDTO(teacher);
    }

    @Override
    public TeacherDTO saveTeacher(TeacherDTO teacherDTO) {
        // Fetch related entities
        User user = teacherDTO.getUserId() != null ?
                userRepository.findById(teacherDTO.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + teacherDTO.getUserId()))
                : null;

        Department department = teacherDTO.getDepartmentId() != null ?
                departmentRepository.findById(teacherDTO.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found with id: " + teacherDTO.getDepartmentId()))
                : null;

        // Map DTO → Entity
        Teacher teacher = TeacherMapper.toEntity(teacherDTO, user, department);

        // Save entity
        Teacher savedTeacher = teacherRepository.save(teacher);

        // Map back Entity → DTO
        return TeacherMapper.toDTO(savedTeacher);
    }
}
