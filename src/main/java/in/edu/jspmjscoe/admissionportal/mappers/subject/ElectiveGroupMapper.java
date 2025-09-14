package in.edu.jspmjscoe.admissionportal.mappers.subject;

import in.edu.jspmjscoe.admissionportal.dtos.subject.ElectiveGroupDTO;
import in.edu.jspmjscoe.admissionportal.model.subject.ElectiveGroup;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ElectiveGroupMapper {

    // ===== Entity -> DTO =====
    @Mapping(source = "subjects", target = "subjectIds")
    ElectiveGroupDTO toDto(ElectiveGroup electiveGroup);

    // ===== DTO -> Entity =====
    @InheritInverseConfiguration
    @Mapping(target = "subjects", expression = "java(mapSubjectsFromIds(dto.getSubjectIds()))")
    ElectiveGroup toEntity(ElectiveGroupDTO dto);

    // ---------- Helper methods ----------
    default List<Long> mapSubjectsToIds(List<Subject> subjects) {
        if (subjects == null) return null;
        return subjects.stream()
                .map(Subject::getSubjectId)
                .collect(Collectors.toList());
    }

    default List<Subject> mapSubjectsFromIds(List<Long> subjectIds) {
        if (subjectIds == null) return null;
        return subjectIds.stream()
                .map(id -> {
                    Subject s = new Subject();
                    s.setSubjectId(id);
                    return s;
                })
                .collect(Collectors.toList());
    }
}
