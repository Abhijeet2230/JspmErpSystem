package in.edu.jspmjscoe.admissionportal.services.impl.trainingplacement;

import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.*;
import in.edu.jspmjscoe.admissionportal.exception.TrainingPlacementNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.trainingplacement.TrainingPlacementMapper;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.*;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.repositories.trainingplacement.TrainingPlacementRecordRepository;
import in.edu.jspmjscoe.admissionportal.services.trainingplacement.TrainingPlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingPlacementServiceImpl implements TrainingPlacementService {

    private final StudentAcademicYearRepository studentAcademicYearRepository;
    private final TrainingPlacementRecordRepository trainingPlacementRecordRepository;
    private final TrainingPlacementMapper trainingPlacementMapper;

    @Override
    @Transactional
    public void initializeTrainingPlacementRecords() {
        List<StudentAcademicYear> studentYears = studentAcademicYearRepository.findByIsActiveTrue();
        List<TrainingPlacementRecord> records = new ArrayList<>();

        for (StudentAcademicYear studentYear : studentYears) {
            TrainingPlacementRecord record = TrainingPlacementRecord.builder()
                    .studentAcademicYear(studentYear)
                    .sgpaScore(0.0)
                    .softskillAttendance(0.0)
                    .certificationCourses(0.0)
                    .allSubjectQuiz(0.0)
                    .internship(0.0)
                    .courseProject(0.0)
                    .nationalEvent(0.0)
                    .totalScore(0.0)
                    .build();

            List<TrainingPlacementTest> tests = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                tests.add(TrainingPlacementTest.builder()
                        .trainingPlacementRecord(record)
                        .category(TestCategory.SOFTSKILL)
                        .testName("Softskill Test" + i)
                        .score(0.0)
                        .build());
            }

            for (int i = 1; i <= 5; i++) {
                tests.add(TrainingPlacementTest.builder()
                        .trainingPlacementRecord(record)
                        .category(TestCategory.APTITUDE)
                        .testName("Aptitude Test" + i)
                        .score(0.0)
                        .build());
            }

            record.setTests(tests);
            records.add(record);
        }

        trainingPlacementRecordRepository.saveAll(records);
    }

    @Override
    public List<StudentPlacementDTO> getStudentsByDivision(String division) {
        List<TrainingPlacementRecord> records = trainingPlacementRecordRepository.findByStudentAcademicYear_Division(division);

        return records.stream()
                .map(r -> StudentPlacementDTO.builder()
                        .studentAcademicYearId(r.getStudentAcademicYear().getStudentAcademicYearId()) // ✅ set it here
                        .rollNo(r.getStudentAcademicYear().getRollNo())
                        .name(r.getStudentAcademicYear().getStudent().getCandidateName())
                        .division(r.getStudentAcademicYear().getDivision())
                        .trainingPlacement(trainingPlacementMapper.toDTO(r))
                        .build())
                .toList();
    }

    @Override
    public List<StudentPlacementDTO> getStudentsByRollNo(Integer rollNo) {
        // Fetch all T&P records for the given rollNo
        List<TrainingPlacementRecord> records = trainingPlacementRecordRepository
                .findByStudentAcademicYear_RollNo(rollNo);

        return records.stream()
                .map(r -> StudentPlacementDTO.builder()
                        .studentAcademicYearId(r.getStudentAcademicYear().getStudentAcademicYearId())
                        .rollNo(r.getStudentAcademicYear().getRollNo())
                        .name(r.getStudentAcademicYear().getStudent().getCandidateName())
                        .division(r.getStudentAcademicYear().getDivision())
                        .trainingPlacement(trainingPlacementMapper.toDTO(r))
                        .build())
                .toList();
    }



    @Override
    @Transactional
    public void bulkPatch(BulkTrainingPlacementPatchRequest request) {
        for (TrainingPlacementPatchDTO dto : request.getUpdates()) {
            TrainingPlacementRecord record = trainingPlacementRecordRepository
                    .findByStudentAcademicYear_StudentAcademicYearId(dto.getStudentAcademicYearId())
                    .orElseThrow(() -> new TrainingPlacementNotFoundException(dto.getStudentAcademicYearId(), null));

            if (dto.getSgpaScore() != null) record.setSgpaScore(dto.getSgpaScore());
            if (dto.getSoftskillAttendance() != null) record.setSoftskillAttendance(dto.getSoftskillAttendance());
            if (dto.getCertificationCourses() != null) record.setCertificationCourses(dto.getCertificationCourses());
            if (dto.getAllSubjectQuiz() != null) record.setAllSubjectQuiz(dto.getAllSubjectQuiz());
            if (dto.getInternship() != null) record.setInternship(dto.getInternship());
            if (dto.getCourseProject() != null) record.setCourseProject(dto.getCourseProject());
            if (dto.getNationalEvent() != null) record.setNationalEvent(dto.getNationalEvent());
            if (dto.getTotalScore() != null) record.setTotalScore(dto.getTotalScore());

            if (dto.getTests() != null) {
                for (TrainingPlacementTestDTO testDto : dto.getTests()) {
                    TrainingPlacementTest test = record.getTests().stream()
                            .filter(t -> t.getTestName().equalsIgnoreCase(testDto.getTestName())
                                    && t.getCategory() == testDto.getCategory())
                            .findFirst()
                            .orElseThrow(() -> new TrainingPlacementNotFoundException(dto.getStudentAcademicYearId(), testDto.getTestName()));

                    if (testDto.getScore() != null) test.setScore(testDto.getScore());
                }
            }

            trainingPlacementRecordRepository.save(record);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StudentPlacementDTO getMyTrainingPlacement(Long studentId) {
        StudentAcademicYear studentYear = studentAcademicYearRepository
                .findByStudent_StudentIdAndIsActiveTrue(studentId)
                .orElseThrow(() -> new TrainingPlacementNotFoundException(studentId, null));

        TrainingPlacementRecord record = trainingPlacementRecordRepository
                .findByStudentAcademicYear_StudentAcademicYearId(studentYear.getStudentAcademicYearId())
                .orElseThrow(() -> new TrainingPlacementNotFoundException(studentYear.getStudentAcademicYearId(), null));

        return StudentPlacementDTO.builder()
                .studentAcademicYearId(studentYear.getStudentAcademicYearId())
                .rollNo(studentYear.getRollNo())
                .name(studentYear.getStudent().getCandidateName())
                .division(studentYear.getDivision())
                .trainingPlacement(trainingPlacementMapper.toDTO(record))
                .build();
    }

}
