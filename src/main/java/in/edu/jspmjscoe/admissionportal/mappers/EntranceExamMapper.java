package in.edu.jspmjscoe.admissionportal.mappers;


import in.edu.jspmjscoe.admissionportal.dtos.EntranceExamDTO;
import in.edu.jspmjscoe.admissionportal.model.EntranceExam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntranceExamMapper {

    @Mapping(source = "student.studentId", target = "studentId")
    EntranceExamDTO toDTO(EntranceExam exam);

    @Mapping(source = "studentId", target = "student.studentId") // if you want reverse
    EntranceExam toEntity(EntranceExamDTO dto);
}
