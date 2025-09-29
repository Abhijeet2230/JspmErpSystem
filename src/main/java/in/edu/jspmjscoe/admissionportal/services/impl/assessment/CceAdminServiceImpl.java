package in.edu.jspmjscoe.admissionportal.services.impl.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.*;
import in.edu.jspmjscoe.admissionportal.dtos.subject.SubjectDTO;
import in.edu.jspmjscoe.admissionportal.exception.ExamNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.UnitAssessmentNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.assessment.*;
import in.edu.jspmjscoe.admissionportal.mappers.subject.SubjectMapper;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentExamRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentUnitAssessmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
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

    private final StudentAcademicYearRepository studentAcademicYearRepository;
    private final SubjectRepository subjectRepository;
    private final StudentUnitAssessmentRepository studentUnitAssessmentRepository;
    private final StudentExamRepository examRepository;

    private final StudentCCEMapper studentCCEMapper;
    private final StudentUnitAssessmentMapper studentUnitAssessmentMapper;
    private final StudentExamMapper studentExamMapper;
    private final UnitMarksMapper unitMarksMapper;
    private final StudentWithUnitsMapper studentWithUnitsMapper;
    private final SubjectMapper subjectMapper;

    // ---------- Helper ----------
    private String getDivisionGroup(String division) {
        if (division == null || division.trim().isEmpty()) return "A";
        char first = Character.toUpperCase(division.trim().charAt(0));
        if (first >= 'A' && first <= 'E') return "A";
        if (first >= 'F' && first <= 'J') return "B";
        return "A";
    }

    // ---------- Students ----------
    @Override
    public List<StudentCCEDTO> getStudentsByDivision(String division) {
        List<StudentAcademicYear> studentYears = studentAcademicYearRepository.findByDivisionAndIsActiveTrue(division);
        return studentYears.stream()
                .map(studentCCEMapper::toDto) // map StudentAcademicYear -> DTO
                .toList();
    }

    // ---------- Subjects ----------
    @Override
    public List<SubjectDTO> getSubjectsForDivision(String division) {
        String group = getDivisionGroup(division);
        return subjectRepository.findAll().stream()
                .filter(sub ->
                        sub.isHasCCE() && // only subjects with hasCCE = true
                                ("ALL".equalsIgnoreCase(sub.getSubjectGroup()) || group.equalsIgnoreCase(sub.getSubjectGroup()))
                )
                .map(subjectMapper::toDto)
                .toList();
    }


    // ---------- Unit Assessments ----------
    @Override
    public List<StudentUnitAssessmentDTO> getUnitAssessmentsForDivisionAndSubject(String division, Long subjectId) {
        List<StudentAcademicYear> studentYears = studentAcademicYearRepository.findByDivisionAndIsActiveTrue(division);
        List<Long> studentYearIds = studentYears.stream()
                .map(StudentAcademicYear::getStudentAcademicYearId)
                .toList();

        return studentUnitAssessmentRepository
                .findByStudentAcademicYearStudentAcademicYearIdInAndSubjectSubjectIdIn(studentYearIds, Collections.singleton(subjectId))
                .stream()
                .map(studentUnitAssessmentMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public StudentUnitAssessmentDTO updateUnitAssessmentMarks(Long unitId, Double quizMarks, Double activityMarks) {
        StudentUnitAssessment unit = studentUnitAssessmentRepository.findById(unitId)
                .orElseThrow(() -> new UnitAssessmentNotFoundException(unitId));

        if (quizMarks != null) unit.setQuizMarks(quizMarks);
        if (activityMarks != null) unit.setActivityMarks(activityMarks);

        studentUnitAssessmentRepository.save(unit);
        return studentUnitAssessmentMapper.toDTO(unit);
    }

    @Override
    public List<StudentWithUnitsDTO> getStudentsWithUnits(String division, Long subjectId) {
        List<StudentAcademicYear> studentYears = studentAcademicYearRepository.findByDivisionAndIsActiveTrue(division);
        if (studentYears.isEmpty()) return Collections.emptyList();

        List<Long> studentYearIds = studentYears.stream()
                .map(StudentAcademicYear::getStudentAcademicYearId)
                .toList();

        List<StudentUnitAssessment> units = studentUnitAssessmentRepository.findByStudentAcademicYearStudentAcademicYearIdInAndSubjectSubjectIdIn(
                studentYearIds, Collections.singleton(subjectId)
        );

        Map<Long, List<UnitMarksDTO>> studentUnitsMap = units.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getStudentAcademicYear().getStudentAcademicYearId(),
                        Collectors.mapping(unitMarksMapper::toDto, Collectors.toList())
                ));

        return studentYears.stream()
                .map(s -> {
                    StudentWithUnitsDTO dto = studentWithUnitsMapper.toDto(s);
                    dto.setUnits(studentUnitsMap.getOrDefault(s.getStudentAcademicYearId(), Collections.emptyList()));
                    return dto;
                })
                .toList();
    }

    // ---------- Exams ----------
    @Override
    public List<StudentExamDTO> getExamsForDivisionAndSubject(String division, Long subjectId) {
        List<StudentAcademicYear> studentYears = studentAcademicYearRepository.findByDivisionAndIsActiveTrue(division);
        List<Long> studentYearIds = studentYears.stream()
                .map(StudentAcademicYear::getStudentAcademicYearId)
                .toList();

        return examRepository.findByStudentAcademicYearStudentAcademicYearIdInAndSubjectSubjectIdIn(studentYearIds, Collections.singleton(subjectId))
                .stream()
                .map(studentExamMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public StudentExamDTO updateExamMarks(Long examId, Double marksObtained) {
        StudentExam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));

        if (marksObtained != null) exam.setMarksObtained(marksObtained);

        examRepository.save(exam);
        return studentExamMapper.toDto(exam);
    }

    // ---------- Attendance ----------


    // ---------- Bulk Updates ----------
    @Override
    @Transactional
    public List<StudentUnitAssessmentDTO> updateMultipleUnitAssessments(List<UnitUpdateRequestDTO> updates) {
        if (updates == null || updates.isEmpty()) return Collections.emptyList();

        List<Long> unitIds = updates.stream().map(UnitUpdateRequestDTO::getUnitId).toList();
        Map<Long, StudentUnitAssessment> existingMap = studentUnitAssessmentRepository.findAllById(unitIds)
                .stream().collect(Collectors.toMap(StudentUnitAssessment::getStudentUnitAssessmentId, u -> u));

        updates.forEach(req -> {
            StudentUnitAssessment unit = existingMap.get(req.getUnitId());
            if (unit == null) throw new UnitAssessmentNotFoundException(req.getUnitId());

            if (req.getQuizMarks() != null) unit.setQuizMarks(req.getQuizMarks());
            if (req.getActivityMarks() != null) unit.setActivityMarks(req.getActivityMarks());
        });

        return studentUnitAssessmentRepository.saveAll(existingMap.values())
                .stream().map(studentUnitAssessmentMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public List<StudentExamDTO> updateExamMarksBulk(List<ExamUpdateRequestDTO> requests) {
        if (requests == null || requests.isEmpty()) return Collections.emptyList();

        List<Long> examIds = requests.stream().map(ExamUpdateRequestDTO::getExamId).toList();
        Map<Long, StudentExam> existingMap = examRepository.findAllById(examIds)
                .stream().collect(Collectors.toMap(StudentExam::getStudentExamId, e -> e));

        requests.forEach(req -> {
            StudentExam exam = existingMap.get(req.getExamId());
            if (exam == null) throw new ExamNotFoundException(req.getExamId());
            exam.setMarksObtained(req.getMarksObtained());
        });

        return examRepository.saveAll(existingMap.values())
                .stream().map(studentExamMapper::toDto)
                .toList();
    }

    @Override
    public List<StudentUnitAssessmentDTO> getAllUnitsForSubject(List<StudentCCEDTO> students, Long subjectId) {
        if (students == null || students.isEmpty()) return Collections.emptyList();

        List<Long> studentYearIds = students.stream()
                .map(StudentCCEDTO::getStudentAcademicYearId)
                .toList();

        return studentUnitAssessmentRepository
                .findByStudentAcademicYearStudentAcademicYearIdInAndSubjectSubjectIdIn(studentYearIds, Collections.singleton(subjectId))
                .stream()
                .map(studentUnitAssessmentMapper::toDTO)
                .toList();
    }
}
