package in.edu.jspmjscoe.admissionportal.services.impl.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.UnitMarksDTO;
import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.ExamMarksResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.StudentCCEProfileResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside.SubjectCCERecordResponseDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
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

    @Override
    public StudentCCEProfileResponseDTO getStudentCCEProfile(Long studentId) {
        // 1️⃣ Fetch Student basic info
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2️⃣ Fetch the current (active) academic year
        StudentAcademicYear studentAcademicYear = student.getStudentAcademicYears().stream()
                .filter(StudentAcademicYear::getIsActive)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Active academic year not found"));

        Long studentAcademicYearId = studentAcademicYear.getStudentAcademicYearId();

        // 3️⃣ Fetch unit assessments for this academic year
        List<StudentUnitAssessment> unitAssessments =
                studentUnitAssessmentRepository.findByStudentAcademicYearStudentAcademicYearId(studentAcademicYearId);

        Map<Long, List<StudentUnitAssessment>> unitsBySubject =
                unitAssessments.stream().collect(Collectors.groupingBy(u -> u.getSubject().getSubjectId()));

        // 4️⃣ Fetch exams for this academic year
        List<StudentExam> exams =
                studentExamRepository.findByStudentAcademicYearStudentAcademicYearId(studentAcademicYearId);

        Map<Long, List<StudentExam>> examsBySubject =
                exams.stream().collect(Collectors.groupingBy(e -> e.getSubject().getSubjectId()));

        // 5️⃣ Build subject-wise records
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

            subjectRecords.add(SubjectCCERecordResponseDTO.builder()
                    .subjectName(entry.getValue().get(0).getSubject().getName())
                    .units(unitDtos)
                    .midTerm(midTerm)
                    .endTerm(endTerm)
                    .build());
        }

        // 6️⃣ Build final response using StudentAcademicYear
        return StudentCCEProfileResponseDTO.builder()
                .studentAcademicYearId(studentAcademicYear.getStudentAcademicYearId())
                .candidateName(student.getCandidateName())
                .rollNo(studentAcademicYear.getRollNo())
                .division(studentAcademicYear.getDivision())
                .subjects(subjectRecords)
                .build();
    }
}
