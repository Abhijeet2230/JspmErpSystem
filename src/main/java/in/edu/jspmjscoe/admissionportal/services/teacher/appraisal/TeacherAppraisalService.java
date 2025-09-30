package in.edu.jspmjscoe.admissionportal.services.teacher.appraisal;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherAppraisalService {

    TeacherAppraisalDTO createAppraisal(TeacherAppraisalDTO dto,
                                        MultipartFile document,
                                        MultipartFile photo,
                                        MultipartFile video);

    TeacherAppraisalDTO getAppraisalById(Long id);

    List<TeacherAppraisalDTO> getAppraisalsByTeacher(Long teacherId);

    List<TeacherAppraisalDTO> getAllAppraisals();
}
