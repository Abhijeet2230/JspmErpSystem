package in.edu.jspmjscoe.admissionportal.services.student;

import in.edu.jspmjscoe.admissionportal.dtos.security.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import java.util.List;

public interface StudentService {

    StudentDTO createStudent(StudentDTO studentDTO);
    StudentDTO getStudentById(Long id);
    List<StudentDTO> getAllStudents();
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    void deleteStudent(Long id);
    void changePassword(String username, ChangePasswordRequest request);
}
