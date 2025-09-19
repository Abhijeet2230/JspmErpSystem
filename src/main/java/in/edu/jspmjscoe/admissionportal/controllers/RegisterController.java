package in.edu.jspmjscoe.admissionportal.controllers;

import in.edu.jspmjscoe.admissionportal.dtos.subject.DepartmentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.mappers.subject.DepartmentMapper;
import in.edu.jspmjscoe.admissionportal.services.subject.DepartmentService;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/public")
@RequiredArgsConstructor
public class RegisterController {

    final TeacherService teacherService;
    final DepartmentService departmentService;
    final DepartmentMapper  departmentMapper;

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments()
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(departments);
    }

    @PostMapping("/saveteacher")
    public ResponseEntity<TeacherDTO> saveTeacher(@RequestBody TeacherDTO teacherDTO) {
        TeacherDTO savedTeacher = teacherService.saveTeacher(teacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher);
    }
}
