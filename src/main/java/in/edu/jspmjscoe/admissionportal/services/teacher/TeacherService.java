package in.edu.jspmjscoe.admissionportal.services.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherSubjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TeacherService {
    List<TeacherDTO> getAllTeachers();
    List<TeacherDTO> getAcceptedTeachers();
    List<TeacherDTO> getPendingTeachers();
    TeacherDTO getTeacherById(Long id);
    // ✅ New method
    TeacherDTO saveTeacher(TeacherDTO teacherDTO);

    Long getCurrentUserId();

    TeacherDTO updateTeacherStatus(Long id, Status status);

    TeacherSubjectDTO assignSubjectToTeacherByName(String teacherName, String subjectName, String division);

    // New Leave Features
    LeaveDTO applyLeave(LeaveDTO leaveDTO);

    @Transactional
    LeaveDTO endLeaveEarly(Long leaveId, LocalTime actualCloserTime);

    List<LeaveDTO> getPendingLeaves();

    List<LeaveDTO> getAcceptedLeaves();

    LeaveDTO updateLeaveStatus(Long id, Status status);


    // --------------- Head Leave -------------//
    HeadLeaveDTO applyHeadLeave(HeadLeaveDTO headLeaveDTO);

    @Transactional
    HeadLeaveDTO endHeadLeaveEarly(Long headLeaveId, LocalDate actualToDate);

    List<HeadLeaveDTO> getPendingHeadLeaves();

    List<HeadLeaveDTO> getAcceptedHeadLeaves();

    HeadLeaveDTO updateHeadLeaveStatus(Long id, Status status);


}
