package in.edu.jspmjscoe.admissionportal.services.impl.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.CceInitResult;
import in.edu.jspmjscoe.admissionportal.model.assessment.Attendance;
import in.edu.jspmjscoe.admissionportal.model.assessment.ExamType;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.AttendanceRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentExamRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentUnitAssessmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CceInitializationService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentUnitAssessmentRepository suaRepository;
    private final StudentExamRepository examRepository;
    private final AttendanceRepository attendanceRepository;

    private static final int UNIT_COUNT = 5;
    private static final int BATCH_SIZE = 500;

    @Transactional
    public CceInitResult initializeAll(boolean createUnits, boolean createExams, boolean createAttendance) {

        List<Student> students = studentRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();

        if (students.isEmpty() || subjects.isEmpty()) {
            return new CceInitResult(0, 0, 0);
        }

        // Map by ID for quick lookup
        Map<Long, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getStudentId, s -> s));
        Map<Long, Subject> subjectMap = subjects.stream().collect(Collectors.toMap(Subject::getSubjectId, s -> s));

        // Determine eligible student-subject pairs based on division
        List<Pair> eligiblePairs = new ArrayList<>();
        for (Student student : students) {
            String studentGroup = getDivisionGroup(student.getDivision());
            for (Subject subject : subjects) {
                String subjectGroup = Optional.ofNullable(subject.getSubjectGroup()).orElse("ALL").toUpperCase();
                if ("ALL".equals(subjectGroup) || subjectGroup.equals(studentGroup)) {
                    eligiblePairs.add(new Pair(student.getStudentId(), subject.getSubjectId()));
                }
            }
        }

        if (eligiblePairs.isEmpty()) return new CceInitResult(0, 0, 0);

        // Fetch existing rows to avoid duplicates
        Set<Long> studentIds = eligiblePairs.stream().map(p -> p.studentId).collect(Collectors.toSet());
        Set<Long> subjectIds = eligiblePairs.stream().map(p -> p.subjectId).collect(Collectors.toSet());

        Set<String> existingUnitKeys = suaRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(studentIds, subjectIds)
                .stream()
                .map(e -> keyForUnit(e.getStudent().getStudentId(), e.getSubject().getSubjectId(), e.getUnitNumber()))
                .collect(Collectors.toSet());

        Set<String> existingExamKeys = examRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(studentIds, subjectIds)
                .stream()
                .map(e -> keyForExam(e.getStudent().getStudentId(), e.getSubject().getSubjectId(), e.getExamType()))
                .collect(Collectors.toSet());

        Set<String> existingAttendanceKeys = attendanceRepository.findByStudentStudentIdInAndSubjectSubjectIdIn(studentIds, subjectIds)
                .stream()
                .map(a -> keyForAttendance(a.getStudent().getStudentId(), a.getSubject().getSubjectId()))
                .collect(Collectors.toSet());

        // Prepare lists to save
        List<StudentUnitAssessment> toSaveUnits = new ArrayList<>();
        List<StudentExam> toSaveExams = new ArrayList<>();
        List<Attendance> toSaveAttendance = new ArrayList<>();

        for (Pair p : eligiblePairs) {
            Student student = studentMap.get(p.studentId);
            Subject subject = subjectMap.get(p.subjectId);

            // --- Unit assessments ---
            if (createUnits) {
                for (int u = 1; u <= UNIT_COUNT; u++) {
                    String key = keyForUnit(p.studentId, p.subjectId, u);
                    if (!existingUnitKeys.contains(key)) {
                        toSaveUnits.add(StudentUnitAssessment.builder()
                                .student(student)
                                .subject(subject)
                                .unitNumber(u)
                                .quizMarks(0.0)
                                .activityMarks(0.0)
                                .build());
                    }
                }
            }

            // --- Exams ---
            if (createExams) {
                for (ExamType et : ExamType.values()) {
                    String key = keyForExam(p.studentId, p.subjectId, et);
                    if (!existingExamKeys.contains(key)) {
                        toSaveExams.add(StudentExam.builder()
                                .student(student)
                                .subject(subject)
                                .examType(et)
                                .marksObtained(0.0)
                                .build());
                    }
                }
            }

            // --- Attendance ---
            if (createAttendance) {
                String key = keyForAttendance(p.studentId, p.subjectId);
                if (!existingAttendanceKeys.contains(key)) {
                    toSaveAttendance.add(Attendance.builder()
                            .student(student)
                            .subject(subject)
                            .totalClasses(0)
                            .attendedClasses(0)
                            .build());
                }
            }
        }

        // --- Batch save ---
        int unitsCreated = batchSaveUnits(toSaveUnits);
        int examsCreated = batchSaveExams(toSaveExams);
        int attendanceCreated = batchSaveAttendance(toSaveAttendance);

        return new CceInitResult(unitsCreated, examsCreated, attendanceCreated);
    }

    // ---------- helpers ----------
    private int batchSaveUnits(List<StudentUnitAssessment> list) {
        for (int i = 0; i < list.size(); i += BATCH_SIZE) {
            suaRepository.saveAll(list.subList(i, Math.min(i + BATCH_SIZE, list.size())));
        }
        return list.size();
    }

    private int batchSaveExams(List<StudentExam> list) {
        for (int i = 0; i < list.size(); i += BATCH_SIZE) {
            examRepository.saveAll(list.subList(i, Math.min(i + BATCH_SIZE, list.size())));
        }
        return list.size();
    }

    private int batchSaveAttendance(List<Attendance> list) {
        for (int i = 0; i < list.size(); i += BATCH_SIZE) {
            attendanceRepository.saveAll(list.subList(i, Math.min(i + BATCH_SIZE, list.size())));
        }
        return list.size();
    }

    private String keyForUnit(Long studentId, Long subjectId, Integer unitNumber) {
        return studentId + "#" + subjectId + "#" + unitNumber;
    }

    private String keyForExam(Long studentId, Long subjectId, ExamType et) {
        return studentId + "#" + subjectId + "#" + et.name();
    }

    private String keyForAttendance(Long studentId, Long subjectId) {
        return studentId + "#" + subjectId;
    }

    // Map division to group
    private String getDivisionGroup(String division) {
        if (division == null || division.isEmpty()) return "A";
        char d = Character.toUpperCase(division.charAt(0));
        if (d >= 'A' && d <= 'E') return "A"; // Divisions A-E
        if (d >= 'F' && d <= 'J') return "B"; // Divisions F-J
        return "A"; // fallback
    }


    // Internal pair holder
    private static class Pair {
        final Long studentId;
        final Long subjectId;
        Pair(Long s, Long t) { studentId = s; subjectId = t; }
    }
}
