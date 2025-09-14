package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.AssessmentTypeDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.AssessmentType;
import in.edu.jspmjscoe.admissionportal.model.assessment.SubjectAssessment;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AssessmentTypeMapper {

    // ===== Entity -> DTO =====
    @Mapping(target = "subjectAssessmentIds",
            expression = "java(mapSubjectAssessmentIds(assessmentType.getSubjectAssessments()))")
    AssessmentTypeDTO toDto(AssessmentType assessmentType);

    // ===== DTO -> Entity =====
    @Mapping(target = "subjectAssessments",
            expression = "java(mapSubjectAssessments(dto.getSubjectAssessmentIds()))")
    AssessmentType toEntity(AssessmentTypeDTO dto);

    // ---------- Helpers ----------
    default List<Long> mapSubjectAssessmentIds(List<SubjectAssessment> subjectAssessments) {
        if (subjectAssessments == null) return null;
        return subjectAssessments.stream()
                .map(SubjectAssessment::getSubjectAssessmentId)
                .collect(Collectors.toList());
    }

    default List<SubjectAssessment> mapSubjectAssessments(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            SubjectAssessment sa = new SubjectAssessment();
            sa.setSubjectAssessmentId(id);
            return sa;
        }).collect(Collectors.toList());
    }
}
