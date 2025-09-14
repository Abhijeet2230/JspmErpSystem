package in.edu.jspmjscoe.admissionportal.mappers.subject;

import in.edu.jspmjscoe.admissionportal.dtos.subject.DepartmentDTO;


import in.edu.jspmjscoe.admissionportal.mappers.teacher.TeacherMapper;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;

import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", uses = {CourseMapper.class , TeacherMapper.class})
public interface DepartmentMapper {

    // Entity -> DTO
    @Mapping(source = "courses", target = "courses")    // full courses
    @Mapping(source = "teachers", target = "teachers")  // full teachers
    DepartmentDTO toDto(Department department);

    // DTO -> Entity
    @Mapping(source = "courses", target = "courses")
    @Mapping(source = "teachers", target = "teachers")
    Department toEntity(DepartmentDTO dto);


}