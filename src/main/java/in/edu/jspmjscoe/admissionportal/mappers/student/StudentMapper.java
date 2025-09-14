package in.edu.jspmjscoe.admissionportal.mappers.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Gender;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {
        ParentMapper.class,
        AddressMapper.class,
        SSCMapper.class,
        HSCMapper.class,
        EntranceExamMapper.class,
        AdmissionMapper.class
    }
)
public interface StudentMapper {  // Entity → DTO
    StudentDTO toDTO(Student student);

    // DTO → Entity
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    Student toEntity(StudentDTO dto);

    // Custom mapper for Gender
    @Named("stringToGender")
    default Gender mapGender(String genderStr) {
        if (genderStr == null) return null;
        try {
            return Gender.valueOf(genderStr.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null; // handle invalid strings gracefully
        }
    }
}
