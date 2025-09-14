package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.SubjectAssessmentDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.SubjectAssessment;
import in.edu.jspmjscoe.admissionportal.model.assessment.AssessmentType;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubjectAssessmentMapper {

    // Entity -> DTO
    @Mapping(target = "subjectId", source = "subject.subjectId")
    @Mapping(target = "assessmentTypeId", source = "assessmentType.assessmentTypeId")
    SubjectAssessmentDTO toDto(SubjectAssessment entity);

    // DTO -> Entity
    @Mapping(target = "subject", source = "subjectId", qualifiedByName = "mapSubject")
    @Mapping(target = "assessmentType", source = "assessmentTypeId", qualifiedByName = "mapAssessmentType")
    @Mapping(target = "studentMarks", ignore = true) // handled separately if needed
    SubjectAssessment toEntity(SubjectAssessmentDTO dto);

    // ======= Helper methods =======
    @Named("mapSubject")
    default Subject mapSubject(Long subjectId) {
        if (subjectId == null) return null;
        return Subject.builder().subjectId(subjectId).build();
    }

    @Named("mapAssessmentType")
    default AssessmentType mapAssessmentType(Long assessmentTypeId) {
        if (assessmentTypeId == null) return null;
        return AssessmentType.builder().assessmentTypeId(assessmentTypeId).build();
    }
}
