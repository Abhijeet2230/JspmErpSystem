package in.edu.jspmjscoe.admissionportal.services.impl.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectBulkUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectUpdateRequestDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import in.edu.jspmjscoe.admissionportal.repositories.assessment.StudentUnitAssessmentRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.admin.AdminFieldProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminFieldProjectServiceImpl implements AdminFieldProjectService {

    private final StudentUnitAssessmentRepository studentUnitAssessmentRepository;

    @Override
    @Transactional
    public void updateFieldProject(FieldProjectUpdateRequestDTO request) {
        StudentUnitAssessment assessment = studentUnitAssessmentRepository
                .findByStudentAcademicYearIdAndSubjectIdAndUnitNumber(
                        request.getStudentAcademicYearId(),
                        request.getSubjectId(),
                        request.getUnitNumber()
                )
                .orElseThrow(() -> new RuntimeException("Unit assessment not found"));

        assessment.setActivityMarks(request.getMarks());
        studentUnitAssessmentRepository.save(assessment);
    }

    @Override
    @Transactional
    public void updateFieldProjectBulk(FieldProjectBulkUpdateRequestDTO request) {
        request.getUnitMarks().forEach((unitNumber, marks) -> {
            StudentUnitAssessment assessment = studentUnitAssessmentRepository
                    .findByStudentAcademicYearIdAndSubjectIdAndUnitNumber(
                            request.getStudentAcademicYearId(),
                            request.getSubjectId(),
                            unitNumber
                    )
                    .orElseThrow(() -> new RuntimeException("Unit assessment not found for unit " + unitNumber));

            assessment.setActivityMarks(marks);
            studentUnitAssessmentRepository.save(assessment);
        });
    }
}
