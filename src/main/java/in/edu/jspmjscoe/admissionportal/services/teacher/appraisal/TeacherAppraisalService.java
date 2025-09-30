package in.edu.jspmjscoe.admissionportal.services.teacher.appraisal;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalMarksDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherAppraisalService {

    TeacherAppraisalDTO createAppraisal(TeacherAppraisalDTO dto,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        MultipartFile document,
                                        MultipartFile photo,
                                        MultipartFile video);

    TeacherAppraisalDTO getAppraisalById(Long id);

    List<TeacherAppraisalDTO> getAppraisalsByTeacher(Long teacherId);

    List<TeacherAppraisalDTO> getAllAppraisals();
    List<TeacherAppraisalDTO> bulkUpdateMarks(List<TeacherAppraisalMarksDTO> marksList);


}
