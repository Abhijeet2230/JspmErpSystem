package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentAssessmentMarksDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentAssessmentMarks;
import in.edu.jspmjscoe.admissionportal.model.assessment.SubjectAssessment;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentAssessmentMarksMapper {

    // ===== Entity -> DTO =====
    @Mapping(target = "studentId", source = "student.studentId")
    @Mapping(target = "subjectAssessmentId", source = "subjectAssessment.subjectAssessmentId")
    StudentAssessmentMarksDTO toDto(StudentAssessmentMarks entity);

    // ===== DTO -> Entity =====
    @Mapping(target = "student", expression = "java(mapStudent(dto.getStudentId()))")
    @Mapping(target = "subjectAssessment", expression = "java(mapSubjectAssessment(dto.getSubjectAssessmentId()))")
    StudentAssessmentMarks toEntity(StudentAssessmentMarksDTO dto);

    // ---------- Helpers ----------
    default Student mapStudent(Long id) {
        if (id == null) return null;
        Student student = new Student();
        student.setStudentId(id);
        return student;
    }

    default SubjectAssessment mapSubjectAssessment(Long id) {
        if (id == null) return null;
        SubjectAssessment sa = new SubjectAssessment();
        sa.setSubjectAssessmentId(id);
        return sa;
    }
}
