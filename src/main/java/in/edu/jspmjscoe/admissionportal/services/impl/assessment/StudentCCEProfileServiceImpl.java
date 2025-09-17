package in.edu.jspmjscoe.admissionportal.services.impl.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.UnitMarksDTO;
import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.ExamMarksResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.StudentCCEProfileResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.SubjectCCERecordResponseDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.Attendance;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.AttendanceRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentExamRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentUnitAssessmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.assessment.StudentCCEProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentCCEProfileServiceImpl implements StudentCCEProfileService {

    private final StudentRepository studentRepository;
    private final StudentUnitAssessmentRepository studentUnitAssessmentRepository;
    private final StudentExamRepository studentExamRepository;
    private final AttendanceRepository attendanceRepository;


    @Override
    public StudentCCEProfileResponseDTO getStudentCCEProfile(Long studentId) {
        // 1. Fetch Student basic info
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Fetch unit assessments
        List<StudentUnitAssessment> unitAssessments =
                studentUnitAssessmentRepository.findByStudentStudentId(studentId);
        Map<Long, List<StudentUnitAssessment>> unitsBySubject =
                unitAssessments.stream().collect(Collectors.groupingBy(u -> u.getSubject().getSubjectId()));

        // 3. Fetch exams
        List<StudentExam> exams = studentExamRepository.findByStudentStudentId(studentId);
        Map<Long, List<StudentExam>> examsBySubject =
                exams.stream().collect(Collectors.groupingBy(e -> e.getSubject().getSubjectId()));

        // 4. Fetch attendance
        List<Attendance> attendanceList = attendanceRepository.findByStudentStudentId(studentId);
        Map<Long, Attendance> attendanceBySubject =
                attendanceList.stream().collect(Collectors.toMap(a -> a.getSubject().getSubjectId(), a -> a));

        // 5. Build subject-wise records
        List<SubjectCCERecordResponseDTO> subjectRecords = new ArrayList<>();
        for (Map.Entry<Long, List<StudentUnitAssessment>> entry : unitsBySubject.entrySet()) {
            Long subjectId = entry.getKey();

            List<UnitMarksDTO> unitDtos = entry.getValue().stream()
                    .map(u -> new UnitMarksDTO(u.getUnitNumber(), u.getQuizMarks(), u.getActivityMarks()))
                    .toList();

            ExamMarksResponseDTO midTerm = examsBySubject.getOrDefault(subjectId, List.of()).stream()
                    .filter(e -> e.getExamType().name().equals("MID_TERM"))
                    .findFirst()
                    .map(e -> new ExamMarksResponseDTO("MID_TERM", e.getMarksObtained()))
                    .orElse(null);

            ExamMarksResponseDTO endTerm = examsBySubject.getOrDefault(subjectId, List.of()).stream()
                    .filter(e -> e.getExamType().name().equals("END_TERM"))
                    .findFirst()
                    .map(e -> new ExamMarksResponseDTO("END_TERM", e.getMarksObtained()))
                    .orElse(null);

            Double attendancePercentage = Optional.ofNullable(attendanceBySubject.get(subjectId))
                    .map(a -> (a.getTotalClasses() != null && a.getTotalClasses() > 0)
                            ? (a.getAttendedClasses() * 100.0 / a.getTotalClasses())
                            : 0.0)
                    .orElse(0.0);

            subjectRecords.add(SubjectCCERecordResponseDTO.builder()
                    .subjectName(entry.getValue().get(0).getSubject().getName()) // adjust field name
                    .attendance(attendancePercentage)
                    .units(unitDtos)
                    .midTerm(midTerm)
                    .endTerm(endTerm)
                    .build());
        }

        // 6. Build final response
        return StudentCCEProfileResponseDTO.builder()
                .studentId(student.getStudentId())
                .candidateName(student.getCandidateName())
                .rollNo(student.getRollNo())
                .division(student.getDivision())
                .subjects(subjectRecords)
                .build();
    }

}
