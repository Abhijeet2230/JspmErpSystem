package in.edu.jspmjscoe.admissionportal.services.student;

import in.edu.jspmjscoe.admissionportal.dtos.security.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileUpdateDTO;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Student;

import java.util.List;

public interface StudentService {


    List<StudentDTO> getAllStudents();
    void changePassword(String username, ChangePasswordRequest request);

    Student updateProfile(User user, StudentProfileUpdateDTO dto);
}
