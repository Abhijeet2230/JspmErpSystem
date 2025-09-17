package in.edu.jspmjscoe.admissionportal.services.impl.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.*;
import in.edu.jspmjscoe.admissionportal.dtos.subject.SubjectDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.Attendance;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.AttendanceRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentExamRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentUnitAssessmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.SubjectRepository;
import in.edu.jspmjscoe.admissionportal.services.assessment.CceAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CceAdminServiceImpl implements CceAdminService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentUnitAssessmentRepository suaRepository;
    private final StudentExamRepository examRepository;
    private final AttendanceRepository attendanceRepository;

    private String getDivisionGroup(String division) {
        if (division == null || division.trim().isEmpty()) return "A";
        char first = Character.toUpperCase(division.trim().charAt(0));
        if (first >= 'A' && first <= 'E') return "A";
        if (first >= 'F' && first <= 'J') return "B";
        return "A";
    }

    // ----------------- Students -----------------
    @Override
    public List<StudentCCEDTO> getStudentsByDivision(String division) {
        List<Student> students = studentRepository.findByDivision(division);
        return students.stream().map(s -> StudentCCEDTO.builder()
                .studentId(s.getStudentId())
                .candidateName(s.getCandidateName())
                .rollNo(s.getRollNo())
                .division(s.getDivision())
                .build()).collect(Collectors.toList());
    }

    // ----------------- Subjects -----------------
    @Override
    public List<SubjectDTO> getSubjectsForDivision(String division) {
        String group = getDivisionGroup(division);
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .filter(sub -> "ALL".equalsIgnoreCase(sub.getSubjectGroup()) ||
                        group.equalsIgnoreCase(sub.getSubjectGroup()))
                .map(sub -> SubjectDTO.builder()
                        .subjectId(sub.getSubjectId())
                        .name(sub.getName())
                        .code(sub.getCode())
                        .theoryCredits(sub.getTheoryCredits())
                        .practicalCredits(sub.getPracticalCredits())
                        .yearOfStudy(sub.getYearOfStudy())
                        .subjectGroup(sub.getSubjectGroup())
                        .semester(sub.getSemester())
                        .subjectType(sub.getSubjectType())
                        .subjectCategory(sub.getSubjectCategory())
                        .totalUnits(sub.getTotalUnits())
                        .status(sub.getStatus())
                        .courseId(sub.getCourse() != null ? sub.getCourse().getCourseId() : null)
                        .build())
                .collect(Collectors.toList());
    }

    // ----------------- Unit Assessments -----------------
    @Override
    public List<StudentUnitAssessmentDTO> getUnitAssessmentsForDivisionAndSubject(String division, Long subjectId) {
        String group = getDivisionGroup(division);
        List<Student> students = studentRepository.findByDivision(division);
        List<Long> studentIds = students.stream().map(Student::getStudentId).collect(Collectors.toList());
        List<StudentUnitAssessment> units = suaRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(studentIds, Collections.singletonList(subjectId));

        return units.stream().map(u -> StudentUnitAssessmentDTO.builder()
                        .id(u.getId())
                        .studentId(u.getStudent().getStudentId())
                        .rollNo(u.getStudent().getRollNo())
                        .candidateName(u.getStudent().getCandidateName())
                        .subjectId(u.getSubject().getSubjectId())
                        .unitNumber(u.getUnitNumber())
                        .quizMarks(u.getQuizMarks())
                        .activityMarks(u.getActivityMarks())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentUnitAssessmentDTO updateUnitAssessmentMarks(Long unitId, Double quizMarks, Double activityMarks) {
        StudentUnitAssessment sua = suaRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit assessment not found: " + unitId));
        if (quizMarks != null) sua.setQuizMarks(quizMarks);
        if (activityMarks != null) sua.setActivityMarks(activityMarks);
        suaRepository.save(sua);
        return StudentUnitAssessmentDTO.builder()
                .id(sua.getId())
                .studentId(sua.getStudent().getStudentId())
                .rollNo(sua.getStudent().getRollNo())
                .candidateName(sua.getStudent().getCandidateName())
                .subjectId(sua.getSubject().getSubjectId())
                .unitNumber(sua.getUnitNumber())
                .quizMarks(sua.getQuizMarks())
                .activityMarks(sua.getActivityMarks())
                .build();
    }

    // ----------------- Exams -----------------
    @Override
    public List<StudentExamDTO> getExamsForDivisionAndSubject(String division, Long subjectId) {
        List<Student> students = studentRepository.findByDivision(division);
        List<Long> studentIds = students.stream().map(Student::getStudentId).collect(Collectors.toList());
        List<StudentExam> exams = examRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(studentIds, Collections.singletonList(subjectId));

        return exams.stream().map(e -> StudentExamDTO.builder()
                .id(e.getId())
                .studentId(e.getStudent().getStudentId())
                .subjectId(e.getSubject().getSubjectId())
                .examType(e.getExamType().name())
                .marksObtained(e.getMarksObtained())
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentExamDTO updateExamMarks(Long examId, Double marksObtained) {
        StudentExam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found: " + examId));
        if (marksObtained != null) exam.setMarksObtained(marksObtained);
        examRepository.save(exam);
        return StudentExamDTO.builder()
                .id(exam.getId())
                .studentId(exam.getStudent().getStudentId())
                .subjectId(exam.getSubject().getSubjectId())
                .examType(exam.getExamType().name())
                .marksObtained(exam.getMarksObtained())
                .build();
    }

    // ----------------- Attendance -----------------
    @Override
    public List<AttendanceDTO> getAttendanceForDivisionAndSubject(String division, Long subjectId) {
        List<Student> students = studentRepository.findByDivision(division);
        List<Long> studentIds = students.stream().map(Student::getStudentId).collect(Collectors.toList());
        List<Attendance> attendances = attendanceRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(studentIds, Collections.singletonList(subjectId));

        return attendances.stream().map(a -> AttendanceDTO.builder()
                .attendanceId(a.getAttendanceId())
                .studentId(a.getStudent().getStudentId())
                .subjectId(a.getSubject().getSubjectId())
                .totalClasses(a.getTotalClasses())
                .attendedClasses(a.getAttendedClasses())
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttendanceDTO updateAttendance(Long attendanceId, Integer totalClasses, Integer attendedClasses) {
        Attendance att = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found: " + attendanceId));
        if (totalClasses != null) att.setTotalClasses(totalClasses);
        if (attendedClasses != null) att.setAttendedClasses(attendedClasses);
        attendanceRepository.save(att);
        return AttendanceDTO.builder()
                .attendanceId(att.getAttendanceId())
                .studentId(att.getStudent().getStudentId())
                .subjectId(att.getSubject().getSubjectId())
                .totalClasses(att.getTotalClasses())
                .attendedClasses(att.getAttendedClasses())
                .build();
    }


    @Override
    public List<StudentUnitAssessmentDTO> getAllUnitsForSubject(List<StudentCCEDTO> students, Long subjectId) {
        if (students.isEmpty()) return Collections.emptyList();

        List<Long> studentIds = students.stream()
                .map(StudentCCEDTO::getStudentId)
                .collect(Collectors.toList());

        List<StudentUnitAssessment> units = suaRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(
                studentIds, Collections.singletonList(subjectId));

        return units.stream().map(u -> StudentUnitAssessmentDTO.builder()
                .id(u.getId())
                .studentId(u.getStudent().getStudentId())
                .rollNo(u.getStudent().getRollNo())
                .candidateName(u.getStudent().getCandidateName())
                .subjectId(u.getSubject().getSubjectId())
                .unitNumber(u.getUnitNumber())
                .quizMarks(u.getQuizMarks())
                .activityMarks(u.getActivityMarks())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<StudentWithUnitsDTO> getStudentsWithUnits(String division, Long subjectId) {
        // 1. Fetch students in the division
        List<StudentCCEDTO> students = getStudentsByDivision(division);
        if (students.isEmpty()) return Collections.emptyList();

        List<Long> studentIds = students.stream()
                .map(StudentCCEDTO::getStudentId)
                .collect(Collectors.toList());

        // 2. Fetch all units for these students and the subject
        List<StudentUnitAssessment> units = suaRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(
                studentIds, Collections.singletonList(subjectId));

        // 3. Group units by studentId
        Map<Long, List<UnitMarksDTO>> studentUnitsMap = units.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getStudent().getStudentId(),
                        Collectors.mapping(
                                u -> UnitMarksDTO.builder()
                                        .unitNumber(u.getUnitNumber())
                                        .quizMarks(u.getQuizMarks())
                                        .activityMarks(u.getActivityMarks())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        // 4. Map students to StudentWithUnitsDTO
        List<StudentWithUnitsDTO> result = students.stream().map(s -> {
            List<UnitMarksDTO> unitList = studentUnitsMap.getOrDefault(s.getStudentId(), new ArrayList<>());
            return StudentWithUnitsDTO.builder()
                    .studentId(s.getStudentId())
                    .candidateName(s.getCandidateName())
                    .rollNo(s.getRollNo())
                    .units(unitList)
                    .build();
        }).collect(Collectors.toList());

        return result;
    }

    public List<StudentUnitAssessmentDTO> updateMultipleUnitAssessments(List<UnitUpdateRequestDTO> updates) {
        List<StudentUnitAssessmentDTO> result = new ArrayList<>();
        for (UnitUpdateRequestDTO req : updates) {
            StudentUnitAssessmentDTO updated = updateUnitAssessmentMarks(req.getUnitId(), req.getQuizMarks(), req.getActivityMarks());
            result.add(updated);
        }
        return result;
    }


    @Override
    @Transactional
    public List<StudentExamDTO> updateExamMarksBulk(List<ExamUpdateRequestDTO> requests) {
        List<StudentExamDTO> updatedList = new ArrayList<>();

        for (ExamUpdateRequestDTO req : requests) {
            StudentExam exam = examRepository.findById(req.getExamId())
                    .orElseThrow(() -> new RuntimeException("Exam not found: " + req.getExamId()));

            exam.setMarksObtained(req.getMarksObtained());
            StudentExam saved = examRepository.save(exam);

            updatedList.add(
                    StudentExamDTO.builder()
                            .id(saved.getId())
                            .studentId(saved.getStudent().getStudentId())
                            .subjectId(saved.getSubject().getSubjectId())
                            .examType(saved.getExamType().name())
                            .marksObtained(saved.getMarksObtained())
                            .build()
            );
        }

        return updatedList;
    }

}
