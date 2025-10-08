package in.edu.jspmjscoe.admissionportal.services.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.*;
import in.edu.jspmjscoe.admissionportal.dtos.subject.SubjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AdminStudentSubjectAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentOverallSubjectAttendanceDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CceAdminService {

    // Students
    List<StudentCCEDTO> getStudentsByDivision(String division);

    // Subjects
    List<SubjectDTO> getSubjectsForDivision(String division);

    // Unit Assessments
    List<StudentUnitAssessmentDTO> getUnitAssessmentsForDivisionAndSubject(String division, Long subjectId);
    StudentUnitAssessmentDTO updateUnitAssessmentMarks(Long unitId, Double quizMarks, Double activityMarks);

    // Exams
    List<StudentExamDTO> getExamsForDivisionAndSubject(String division, Long subjectId);
    StudentExamDTO updateExamMarks(Long examId, Double marksObtained);

    List<StudentUnitAssessmentDTO> getAllUnitsForSubject(List<StudentCCEDTO> students, Long subjectId);

    List<StudentWithUnitsDTO> getStudentsWithUnits(String division, Long subjectId);

    List<StudentUnitAssessmentDTO> updateMultipleUnitAssessments(List<UnitUpdateRequestDTO> updates);

    List<StudentExamDTO> updateExamMarksBulk(List<ExamUpdateRequestDTO> requests);
    List<SubjectDTO> getSubjectsForLoggedInStudent(UserDetails userDetails);
    List<AdminStudentOverallSubjectAttendanceDTO> getOverallAttendanceForDivisionAndSubject(String division, Long subjectId);

}
