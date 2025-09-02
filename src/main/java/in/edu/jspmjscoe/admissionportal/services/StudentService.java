package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.StudentDTO;
import java.util.List;

public interface StudentService {

    StudentDTO createStudent(StudentDTO studentDTO);
    StudentDTO getStudentById(Long id);
    List<StudentDTO> getAllStudents();
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    void deleteStudent(Long id);
}
