package in.edu.jspmjscoe.admissionportal.services.impl.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.*;
import in.edu.jspmjscoe.admissionportal.exception.achievement.FieldProjectMarkException;
import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.FieldProjectRepository;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentUnitAssessmentRepository;
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
        if (request.getMarks() == null || request.getMarks() < 0 || request.getMarks() > 10) {
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

        // 4️⃣ Return single update DTO
        return new FieldProjectSingleUpdateResultDTO(
                fieldProject.getFieldProjectId(),
                request.getStudentAcademicYearId(),
                request.getSubjectId(),
                request.getUnitNumber(),
                request.getMarks()
        );
    }
}
