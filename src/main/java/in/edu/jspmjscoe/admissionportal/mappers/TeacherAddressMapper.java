package in.edu.jspmjscoe.admissionportal.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import in.edu.jspmjscoe.admissionportal.model.TeacherAddress;
import in.edu.jspmjscoe.admissionportal.model.Teacher;
import in.edu.jspmjscoe.admissionportal.dtos.TeacherAddressDTO;

@Mapper(componentModel = "spring")
public interface TeacherAddressMapper {

    // Entity -> DTO
    @Mapping(source = "teacher.teacherId", target = "teacherId")
    TeacherAddressDTO toDTO(TeacherAddress entity);

    // DTO -> Entity
    @Mapping(source = "teacherId", target = "teacher", qualifiedByName = "mapTeacher")
    TeacherAddress toEntity(TeacherAddressDTO dto);

    // ✅ Custom method: create Teacher from teacherId
    @Named("mapTeacher")
    default Teacher mapTeacher(Long teacherId) {
        if (teacherId == null) return null;
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        return teacher;
    }
}
