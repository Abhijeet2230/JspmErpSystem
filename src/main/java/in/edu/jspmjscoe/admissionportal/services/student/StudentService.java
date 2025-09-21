package in.edu.jspmjscoe.admissionportal.services.student;

import in.edu.jspmjscoe.admissionportal.dtos.security.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import java.util.List;

public interface StudentService {


    List<StudentDTO> getAllStudents();
    void changePassword(String username, ChangePasswordRequest request);
}
