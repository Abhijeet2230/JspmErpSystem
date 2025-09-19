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
    private final StudentUnitAssessmentRepository studentUnitAssessmentRepository;
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
        List<StudentUnitAssessment> units = studentUnitAssessmentRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(studentIds, Collections.singletonList(subjectId));

        return units.stream().map(studentUnitAssessment -> StudentUnitAssessmentDTO.builder()
                        .id(studentUnitAssessment.getId())
                        .studentId(studentUnitAssessment.getStudent().getStudentId())
                        .rollNo(studentUnitAssessment.getStudent().getRollNo())
                        .candidateName(studentUnitAssessment.getStudent().getCandidateName())
                        .subjectId(studentUnitAssessment.getSubject().getSubjectId())
                        .unitNumber(studentUnitAssessment.getUnitNumber())
                        .quizMarks(studentUnitAssessment.getQuizMarks())
                        .activityMarks(studentUnitAssessment.getActivityMarks())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentUnitAssessmentDTO updateUnitAssessmentMarks(Long unitId, Double quizMarks, Double activityMarks) {
        StudentUnitAssessment studentUnitAssessment = studentUnitAssessmentRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit assessment not found: " + unitId));
        if (quizMarks != null) studentUnitAssessment.setQuizMarks(quizMarks);
        if (activityMarks != null) studentUnitAssessment.setActivityMarks(activityMarks);
        studentUnitAssessmentRepository.save(studentUnitAssessment);
        return StudentUnitAssessmentDTO.builder()
                .id(studentUnitAssessment.getId())
                .studentId(studentUnitAssessment.getStudent().getStudentId())
                .rollNo(studentUnitAssessment.getStudent().getRollNo())
                .candidateName(studentUnitAssessment.getStudent().getCandidateName())
                .subjectId(studentUnitAssessment.getSubject().getSubjectId())
                .unitNumber(studentUnitAssessment.getUnitNumber())
                .quizMarks(studentUnitAssessment.getQuizMarks())
                .activityMarks(studentUnitAssessment.getActivityMarks())
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

        List<StudentUnitAssessment> units = studentUnitAssessmentRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(
                studentIds, Collections.singletonList(subjectId));

        return units.stream().map(studentUnitAssessment -> StudentUnitAssessmentDTO.builder()
                .id(studentUnitAssessment.getId())
                .studentId(studentUnitAssessment.getStudent().getStudentId())
                .rollNo(studentUnitAssessment.getStudent().getRollNo())
                .candidateName(studentUnitAssessment.getStudent().getCandidateName())
                .subjectId(studentUnitAssessment.getSubject().getSubjectId())
                .unitNumber(studentUnitAssessment.getUnitNumber())
                .quizMarks(studentUnitAssessment.getQuizMarks())
                .activityMarks(studentUnitAssessment.getActivityMarks())
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
        List<StudentUnitAssessment> units = studentUnitAssessmentRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(
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
        return students.stream().map(s -> {
            List<UnitMarksDTO> unitList = studentUnitsMap.getOrDefault(s.getStudentId(), new ArrayList<>());
            return StudentWithUnitsDTO.builder()
                    .studentId(s.getStudentId())
                    .candidateName(s.getCandidateName())
                    .rollNo(s.getRollNo())
                    .units(unitList)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<StudentUnitAssessmentDTO> updateMultipleUnitAssessments(List<UnitUpdateRequestDTO> updates) {
        if (updates == null || updates.isEmpty()) return Collections.emptyList();

        // Fetch all entities in one go
        List<Long> unitIds = updates.stream().map(UnitUpdateRequestDTO::getUnitId).toList();
        Map<Long, StudentUnitAssessment> existingMap = studentUnitAssessmentRepository.findAllById(unitIds)
                .stream()
                .collect(Collectors.toMap(StudentUnitAssessment::getId, u -> u));

        // Apply updates
        for (UnitUpdateRequestDTO req : updates) {
            StudentUnitAssessment studentUnitAssessment = existingMap.get(req.getUnitId());
            if (studentUnitAssessment == null) {
                throw new RuntimeException("Unit assessment not found: " + req.getUnitId());
            }
            if (req.getQuizMarks() != null) studentUnitAssessment.setQuizMarks(req.getQuizMarks());
            if (req.getActivityMarks() != null) studentUnitAssessment.setActivityMarks(req.getActivityMarks());
        }

        // Save all in batch
        List<StudentUnitAssessment> saved = studentUnitAssessmentRepository.saveAll(existingMap.values());

        return saved.stream().map(studentUnitAssessment -> StudentUnitAssessmentDTO.builder()
                .id(studentUnitAssessment.getId())
                .studentId(studentUnitAssessment.getStudent().getStudentId())
                .rollNo(studentUnitAssessment.getStudent().getRollNo())
                .candidateName(studentUnitAssessment.getStudent().getCandidateName())
                .subjectId(studentUnitAssessment.getSubject().getSubjectId())
                .unitNumber(studentUnitAssessment.getUnitNumber())
                .quizMarks(studentUnitAssessment.getQuizMarks())
                .activityMarks(studentUnitAssessment.getActivityMarks())
                .build()).toList();
    }

    @Override
    @Transactional
    public List<StudentExamDTO> updateExamMarksBulk(List<ExamUpdateRequestDTO> requests) {
        if (requests == null || requests.isEmpty()) return Collections.emptyList();

        // Fetch all entities in one go
        List<Long> examIds = requests.stream().map(ExamUpdateRequestDTO::getExamId).toList();
        Map<Long, StudentExam> existingMap = examRepository.findAllById(examIds)
                .stream()
                .collect(Collectors.toMap(StudentExam::getId, e -> e));

        // Apply updates
        for (ExamUpdateRequestDTO req : requests) {
            StudentExam exam = existingMap.get(req.getExamId());
            if (exam == null) {
                throw new RuntimeException("Exam not found: " + req.getExamId());
            }
            exam.setMarksObtained(req.getMarksObtained());
        }

        // Save all in batch
        List<StudentExam> saved = examRepository.saveAll(existingMap.values());

        return saved.stream().map(e -> StudentExamDTO.builder()
                .id(e.getId())
                .studentId(e.getStudent().getStudentId())
                .subjectId(e.getSubject().getSubjectId())
                .examType(e.getExamType().name())
                .marksObtained(e.getMarksObtained())
                .build()).toList();
    }
}
