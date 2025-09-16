package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentExamDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentExam;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentExamMapper {

    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    @Mapping(source = "examType", target = "examType")
    StudentExamDTO toDTO(StudentExam entity);

    @InheritInverseConfiguration
    @Mapping(target = "student", ignore = true) // set in service layer
    @Mapping(target = "subject", ignore = true) // set in service layer
    StudentExam toEntity(StudentExamDTO dto);
}
