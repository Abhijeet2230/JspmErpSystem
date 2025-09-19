package in.edu.jspmjscoe.admissionportal.services.impl.trainingplacement;

import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.BulkTrainingPlacementPatchRequest;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.StudentPlacementDTO;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.TrainingPlacementPatchDTO;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.TrainingPlacementTestDTO;
import in.edu.jspmjscoe.admissionportal.mappers.trainingplacement.TrainingPlacementMapper;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TrainingPlacementRecord;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TrainingPlacementTest;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TestCategory;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
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

    private final StudentRepository studentRepository;
    private final TrainingPlacementRecordRepository trainingPlacementRecordRepository;

    private final TrainingPlacementMapper trainingPlacementMapper;

    @Override
    @Transactional
    public void initializeTrainingPlacementRecords() {
        List<Student> students = studentRepository.findAll();

        List<TrainingPlacementRecord> records = new ArrayList<>();

        for (Student student : students) {
            TrainingPlacementRecord record = TrainingPlacementRecord.builder()
                    .student(student)
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

            // Add 3 Softskill Tests
            for (int i = 1; i <= 3; i++) {
                tests.add(TrainingPlacementTest.builder()
                        .trainingPlacementRecord(record)
                        .category(TestCategory.SOFTSKILL)
                        .testName("Softskill Test" + i)
                        .score(0.0)
                        .build());
            }

            // Add 5 Aptitude Tests
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
        List<TrainingPlacementRecord> records = trainingPlacementRecordRepository.findByDivision(division);

        return records.stream()
                .map(r -> StudentPlacementDTO.builder()
                        .rollNo(r.getStudent().getRollNo())
                        .name(r.getStudent().getCandidateName())
                        .division(r.getStudent().getDivision())
                        .trainingPlacement(trainingPlacementMapper.toDTO(r))
                        .build())
                .toList();
    }


    @Override
    @Transactional
    public void bulkPatch(BulkTrainingPlacementPatchRequest request) {
        for (TrainingPlacementPatchDTO dto : request.getUpdates()) {
            TrainingPlacementRecord record =
                    trainingPlacementRecordRepository.findByStudent_StudentId(dto.getStudentId())
                            .orElseThrow(() -> new RuntimeException("Record not found for studentId=" + dto.getStudentId()));

            // update scalar fields
            if (dto.getSgpaScore() != null) record.setSgpaScore(dto.getSgpaScore());
            if (dto.getSoftskillAttendance() != null) record.setSoftskillAttendance(dto.getSoftskillAttendance());
            if (dto.getCertificationCourses() != null) record.setCertificationCourses(dto.getCertificationCourses());
            if (dto.getAllSubjectQuiz() != null) record.setAllSubjectQuiz(dto.getAllSubjectQuiz());
            if (dto.getInternship() != null) record.setInternship(dto.getInternship());
            if (dto.getCourseProject() != null) record.setCourseProject(dto.getCourseProject());
            if (dto.getNationalEvent() != null) record.setNationalEvent(dto.getNationalEvent());
            if (dto.getTotalScore() != null) record.setTotalScore(dto.getTotalScore());

            // update test scores
            if (dto.getTests() != null) {
                for (TrainingPlacementTestDTO testDto : dto.getTests()) {
                    TrainingPlacementTest test =
                            record.getTests().stream()
                                    .filter(t -> t.getTestName().equalsIgnoreCase(testDto.getTestName())
                                            && t.getCategory() == testDto.getCategory())
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Test not found: " + testDto.getTestName()));

                    if (testDto.getScore() != null) {
                        test.setScore(testDto.getScore());
                    }
                }
            }

            trainingPlacementRecordRepository.save(record); // cascade will save tests too
        }
    }


    @Override
    @Transactional(readOnly = true)
    public StudentPlacementDTO getMyTrainingPlacement(Long studentId) {
        TrainingPlacementRecord record = trainingPlacementRecordRepository
                .findByStudent_StudentId(studentId)
                .orElseThrow(() -> new RuntimeException("No TrainingPlacementRecord found for studentId=" + studentId));

        return StudentPlacementDTO.builder()
                .rollNo(record.getStudent().getRollNo())
                .name(record.getStudent().getCandidateName())
                .division(record.getStudent().getDivision())
                .trainingPlacement(trainingPlacementMapper.toDTO(record))
                .build();
    }



}
