package in.edu.jspmjscoe.admissionportal.mappers.student;


import in.edu.jspmjscoe.admissionportal.dtos.student.EntranceExamDTO;
import in.edu.jspmjscoe.admissionportal.model.student.EntranceExam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntranceExamMapper {

    @Mapping(source = "student.studentId", target = "studentId")
    EntranceExamDTO toDTO(EntranceExam exam);

    @Mapping(source = "studentId", target = "student.studentId") // if you want reverse
    EntranceExam toEntity(EntranceExamDTO dto);
}
