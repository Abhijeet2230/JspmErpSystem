package in.edu.jspmjscoe.admissionportal.services.impl.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.request.FieldProjectBulkUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.request.FieldProjectUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.response.FieldProjectBulkUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.response.FieldProjectSingleUpdateResultDTO;
import in.edu.jspmjscoe.admissionportal.exception.achievement.FieldProjectMarkException;
import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TrainingPlacementRecord;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.FieldProjectRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentUnitAssessmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.trainingplacement.TrainingPlacementRecordRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminFieldProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminFieldProjectServiceImpl implements AdminFieldProjectService {

    private final FieldProjectRepository fieldProjectRepository;
    private final StudentUnitAssessmentRepository studentUnitAssessmentRepository;
    private final TrainingPlacementRecordRepository trainingPlacementRecordRepository;
    private final StudentAcademicYearRepository studentAcademicYearRepository;

    private static final double MAX_MARK_PER_UNIT = 10.0;

    // Single update
    @Override
    @Transactional
    public FieldProjectSingleUpdateResultDTO updateFieldProject(FieldProjectUpdateRequestDTO request) {
        return processSingleUpdate(request);
    }

    // Bulk update
    @Override
    @Transactional
    public FieldProjectBulkUpdateResultDTO updateFieldProjectsBulk(FieldProjectBulkUpdateRequestDTO bulkRequest) {
        if (bulkRequest.getUpdates() == null || bulkRequest.getUpdates().isEmpty()) {
            throw new FieldProjectMarkException("Bulk update request is empty");
        }

        List<FieldProjectSingleUpdateResultDTO> resultList = new ArrayList<>();
        for (FieldProjectUpdateRequestDTO request : bulkRequest.getUpdates()) {
            FieldProjectSingleUpdateResultDTO result = processSingleUpdate(request);
            resultList.add(result);
        }

        return new FieldProjectBulkUpdateResultDTO(resultList);
    }

    // Helper method to process single update
    private FieldProjectSingleUpdateResultDTO processSingleUpdate(FieldProjectUpdateRequestDTO request) {
        // 1️⃣ Validate marks
        if (request.getMarks() == null || request.getMarks() < 0 || request.getMarks() > MAX_MARK_PER_UNIT) {
            throw new FieldProjectMarkException("Invalid marks: " + request.getMarks() + ". Allowed range 0–10");
        }

        // 2️⃣ Update FieldProject table
        FieldProject fieldProject = fieldProjectRepository.findById(request.getFieldProjectId())
                .orElseThrow(() -> new FieldProjectMarkException("FieldProject not found: " + request.getFieldProjectId()));
        fieldProject.setMarks(request.getMarks());
        fieldProjectRepository.save(fieldProject);

        // 3️⃣ Update CCE table (StudentUnitAssessment)
        StudentUnitAssessment assessment = studentUnitAssessmentRepository
                .findByStudentAcademicYear_StudentAcademicYearIdAndSubject_SubjectIdAndUnitNumber(
                        request.getStudentAcademicYearId(),
                        request.getSubjectId(),
                        request.getUnitNumber()
                )
                .orElseThrow(() -> new FieldProjectMarkException(
                        "Unit assessment not found for StudentAcademicYear=" + request.getStudentAcademicYearId()
                                + ", Subject=" + request.getSubjectId()
                                + ", Unit=" + request.getUnitNumber()
                ));
        assessment.setActivityMarks(request.getMarks());
        studentUnitAssessmentRepository.save(assessment);

        // 4️⃣ Update TrainingPlacementRecord courseProject score
        recalcAndSaveCourseProjectScore(request.getStudentAcademicYearId());

        // 5️⃣ Return single update DTO
        return new FieldProjectSingleUpdateResultDTO(
                fieldProject.getFieldProjectId(),
                request.getStudentAcademicYearId(),
                request.getSubjectId(),
                request.getUnitNumber(),
                request.getMarks()
        );
    }

    // Recalculate courseProject score for student
    private void recalcAndSaveCourseProjectScore(Long studentAcademicYearId) {
        List<FieldProject> allFieldProjects = fieldProjectRepository
                .findByStudentAcademicYear_StudentAcademicYearId(studentAcademicYearId);

        if (allFieldProjects.isEmpty()) {
            return; // nothing to calculate
        }

        double totalObtained = allFieldProjects.stream()
                .filter(fp -> fp.getMarks() != null)
                .mapToDouble(FieldProject::getMarks)
                .sum();

        double convertedScore = (totalObtained / 250.0) * 10.0;
        double convertedScoreRounded = Math.round(convertedScore * 100.0) / 100.0;

        TrainingPlacementRecord tpr = trainingPlacementRecordRepository
                .findByStudentAcademicYear_StudentAcademicYearId(studentAcademicYearId)
                .orElseGet(() -> {
                    TrainingPlacementRecord r = new TrainingPlacementRecord();
                    r.setStudentAcademicYear(
                            studentAcademicYearRepository.findById(studentAcademicYearId)
                                    .orElseThrow(() -> new FieldProjectMarkException("StudentAcademicYear not found"))
                    );
                    return r;
                });

        tpr.setCourseProject(convertedScoreRounded);
        trainingPlacementRecordRepository.save(tpr);
    }
}
