package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentExamDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import in.edu.jspmjscoe.admissionportal.model.assessment.ExamType;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentExamMapper {

    // ---------- Entity -> DTO ----------
    @Mapping(source = "studentExamId", target = "studentExamId")
    @Mapping(source = "studentAcademicYear.studentAcademicYearId", target = "studentAcademicYearId")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    @Mapping(source = "examType", target = "examType", qualifiedByName = "enumToString")
    StudentExamDTO toDto(StudentExam entity);

    // ---------- DTO -> Entity ----------
    @Mapping(source = "studentAcademicYearId", target = "studentAcademicYear", ignore = true) // set in service
    @Mapping(source = "subjectId", target = "subject", ignore = true)                        // set in service
    @Mapping(source = "examType", target = "examType", qualifiedByName = "stringToEnum")
    StudentExam toEntity(StudentExamDTO dto);

    // ---------- Update from DTO (null-safe) ----------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "examType", target = "examType", qualifiedByName = "stringToEnum")
    void updateFromDto(StudentExamDTO dto, @MappingTarget StudentExam entity);

    // ---------- Helpers for enum <-> string ----------
    @Named("enumToString")
    static String enumToString(ExamType examType) {
        return examType != null ? examType.name() : null;
    }

    @Named("stringToEnum")
    static ExamType stringToEnum(String examType) {
        return examType != null ? ExamType.valueOf(examType) : null;
    }
}
