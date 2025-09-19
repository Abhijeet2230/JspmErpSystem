package in.edu.jspmjscoe.admissionportal.services.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.model.security.Status;

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
    // New Leave Features
    LeaveDTO applyLeave(LeaveDTO leaveDTO);


    List<LeaveDTO> getPendingLeaves();

    List<LeaveDTO> getAcceptedLeaves();

    LeaveDTO updateLeaveStatus(Long id, Status status);


    // --------------- Head Leave -------------//
    HeadLeaveDTO applyHeadLeave(HeadLeaveDTO headLeaveDTO);

    List<HeadLeaveDTO> getPendingHeadLeaves();

    List<HeadLeaveDTO> getAcceptedHeadLeaves();

    HeadLeaveDTO updateHeadLeaveStatus(Long id, Status status);
}
