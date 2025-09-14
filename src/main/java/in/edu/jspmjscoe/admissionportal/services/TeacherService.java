package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.TeacherDTO;

import java.util.List;

public interface TeacherService {
    List<TeacherDTO> getAllTeachers();
    TeacherDTO getTeacherById(Long id);
    // ✅ New method
    TeacherDTO saveTeacher(TeacherDTO teacherDTO);
}
