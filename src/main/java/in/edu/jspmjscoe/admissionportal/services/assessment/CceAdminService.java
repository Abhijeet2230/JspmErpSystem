package in.edu.jspmjscoe.admissionportal.services.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.*;
import in.edu.jspmjscoe.admissionportal.dtos.subject.SubjectDTO;

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

    // Attendance
    List<AttendanceDTO> getAttendanceForDivisionAndSubject(String division, Long subjectId);
    AttendanceDTO updateAttendance(Long attendanceId, Integer totalClasses, Integer attendedClasses);

    List<StudentUnitAssessmentDTO> getAllUnitsForSubject(List<StudentCCEDTO> students, Long subjectId);

    List<StudentWithUnitsDTO> getStudentsWithUnits(String division, Long subjectId);

    List<StudentUnitAssessmentDTO> updateMultipleUnitAssessments(List<UnitUpdateRequestDTO> updates);

    List<StudentExamDTO> updateExamMarksBulk(List<ExamUpdateRequestDTO> requests);
}