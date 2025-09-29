package in.edu.jspmjscoe.admissionportal.services.impl.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.CceInitResult;
import in.edu.jspmjscoe.admissionportal.model.assessment.ExamType;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentExamRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentUnitAssessmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CceInitializationService {

    private final StudentAcademicYearRepository studentAcademicYearRepository;
    private final SubjectRepository subjectRepository;
    private final StudentUnitAssessmentRepository suaRepository;
    private final StudentExamRepository examRepository;

    private static final int UNIT_COUNT = 5;
    private static final int BATCH_SIZE = 500;

    @Transactional
    public CceInitResult initializeAll(boolean createUnits, boolean createExams) {

        List<StudentAcademicYear> studentYears = studentAcademicYearRepository.findAll();
        List<Subject> subjects = subjectRepository.findByHasCCETrue();

        if (studentYears.isEmpty() || subjects.isEmpty()) {
            return new CceInitResult(0, 0);
        }

        // Map by ID for quick lookup
        Map<Long, StudentAcademicYear> studentYearMap = studentYears.stream()
                .collect(Collectors.toMap(StudentAcademicYear::getStudentAcademicYearId, s -> s));
        Map<Long, Subject> subjectMap = subjects.stream()
                .collect(Collectors.toMap(Subject::getSubjectId, s -> s));

        // Determine eligible pairs based on year + semester
        List<Pair> eligiblePairs = new ArrayList<>();
        for (StudentAcademicYear sa : studentYears) {
            for (Subject subject : subjects) {
                if (subject.getYearOfStudy().equals(sa.getYearOfStudy())
                        && subject.getSemester().equals(sa.getSemester())) {
                    eligiblePairs.add(new Pair(sa.getStudentAcademicYearId(), subject.getSubjectId()));
                }
            }
        }

        if (eligiblePairs.isEmpty()) return new CceInitResult(0, 0);

        // Fetch existing rows to avoid duplicates
        Set<Long> studentYearIds = eligiblePairs.stream().map(p -> p.studentAcademicYearId).collect(Collectors.toSet());
        Set<Long> subjectIds = eligiblePairs.stream().map(p -> p.subjectId).collect(Collectors.toSet());

        Set<String> existingUnitKeys = suaRepository
                .findByStudentAcademicYearStudentAcademicYearIdInAndSubjectSubjectIdIn(studentYearIds, subjectIds)
                .stream()
                .map(e -> keyForUnit(
                        e.getStudentAcademicYear().getStudentAcademicYearId(),
                        e.getSubject().getSubjectId(),
                        e.getUnitNumber()))
                .collect(Collectors.toSet());

        Set<String> existingExamKeys = examRepository
                .findByStudentAcademicYearStudentAcademicYearIdInAndSubjectSubjectIdIn(studentYearIds, subjectIds)
                .stream()
                .map(e -> keyForExam(
                        e.getStudentAcademicYear().getStudentAcademicYearId(),
                        e.getSubject().getSubjectId(),
                        e.getExamType()))
                .collect(Collectors.toSet());

        // Prepare lists to save
        List<StudentUnitAssessment> toSaveUnits = new ArrayList<>();
        List<StudentExam> toSaveExams = new ArrayList<>();

        for (Pair p : eligiblePairs) {
            StudentAcademicYear sa = studentYearMap.get(p.studentAcademicYearId);
            Subject subject = subjectMap.get(p.subjectId);

            // --- Unit assessments ---
            if (createUnits) {
                for (int u = 1; u <= UNIT_COUNT; u++) {
                    String key = keyForUnit(p.studentAcademicYearId, p.subjectId, u);
                    if (!existingUnitKeys.contains(key)) {
                        toSaveUnits.add(StudentUnitAssessment.builder()
                                .studentAcademicYear(sa)
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
                    String key = keyForExam(p.studentAcademicYearId, p.subjectId, et);
                    if (!existingExamKeys.contains(key)) {
                        toSaveExams.add(StudentExam.builder()
                                .studentAcademicYear(sa)
                                .subject(subject)
                                .examType(et)
                                .marksObtained(0.0)
                                .build());
                    }
                }
            }
        }

        // --- Batch save ---
        int unitsCreated = batchSaveUnits(toSaveUnits);
        int examsCreated = batchSaveExams(toSaveExams);

        return new CceInitResult(unitsCreated, examsCreated);
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

    private String keyForUnit(Long studentAcademicYearId, Long subjectId, Integer unitNumber) {
        return studentAcademicYearId + "#" + subjectId + "#" + unitNumber;
    }

    private String keyForExam(Long studentAcademicYearId, Long subjectId, ExamType et) {
        return studentAcademicYearId + "#" + subjectId + "#" + et.name();
    }

    // Internal pair holder
    private static class Pair {
        final Long studentAcademicYearId;
        final Long subjectId;
        Pair(Long s, Long t) { studentAcademicYearId = s; subjectId = t; }
    }
}
