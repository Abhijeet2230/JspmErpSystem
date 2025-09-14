package in.edu.jspmjscoe.admissionportal.controllers;

import in.edu.jspmjscoe.admissionportal.dtos.subject.CourseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.subject.DepartmentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.subject.SubjectDTO;
import in.edu.jspmjscoe.admissionportal.mappers.subject.CourseMapper;
import in.edu.jspmjscoe.admissionportal.mappers.subject.DepartmentMapper;
import in.edu.jspmjscoe.admissionportal.mappers.subject.SubjectMapper;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.services.subject.CourseService;
import in.edu.jspmjscoe.admissionportal.services.subject.DepartmentService;
import in.edu.jspmjscoe.admissionportal.services.subject.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/academic")
public class AcademicController {

    private final DepartmentService departmentService;
    private final CourseService courseService;
    private final SubjectService subjectService;
    private final DepartmentMapper departmentMapper;
    private final CourseMapper courseMapper;
    private final SubjectMapper subjectMapper;


    // ------------------ Department ------------------
    @PostMapping("/departments")
    public ResponseEntity<DepartmentDTO> addDepartment(@RequestBody DepartmentDTO dto) {
        Department department = departmentMapper.toEntity(dto);
        Department saved = departmentService.saveDepartment(department);
        return ResponseEntity.ok(departmentMapper.toDto(saved));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments()
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(departments);
    }

    // ------------------ Course ------------------
    @PostMapping("/courses")
    public ResponseEntity<CourseDTO> addCourse(@RequestBody CourseDTO dto) {
        Course course = courseMapper.toEntity(dto);
        Course saved = courseService.saveCourse(course);
        return ResponseEntity.ok(courseMapper.toDto(saved));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses()
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    // ------------------ Subject ------------------
    @PostMapping("/subjects")
    public ResponseEntity<SubjectDTO> addSubject(@RequestBody SubjectDTO dto) {
        Subject subject = subjectMapper.toEntity(dto);
        Subject saved = subjectService.saveSubject(subject);
        return ResponseEntity.ok(subjectMapper.toDto(saved));
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects()
                .stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subjects);
    }
}
