package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.ConsultancyProjectApplicationDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.ConsultancyProjectApplication;
import in.edu.jspmjscoe.admissionportal.model.internship.ConsultancyProjectWork;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultancyProjectApplicationMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "student.candidateName", target = "studentName")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.projectTitle", target = "projectTitle")
    @Mapping(source = "project.organization", target = "organization")
    ConsultancyProjectApplicationDTO toDTO(ConsultancyProjectApplication consultancyProjectApplication);

    // DTO → Entity
    @Mapping(target = "student", expression = "java(mapStudent(dto.getStudentId()))")
    @Mapping(target = "project", expression = "java(mapProject(dto.getProjectId()))")
    ConsultancyProjectApplication toEntity(ConsultancyProjectApplicationDTO dto);

    // Helper methods to create references with just IDs
    default Student mapStudent(Long studentId) {
        if (studentId == null) return null;
        Student student = new Student();
        student.setStudentId(studentId);
        return student;
    }

    default ConsultancyProjectWork mapProject(Long projectId) {
        if (projectId == null) return null;
        ConsultancyProjectWork project = new ConsultancyProjectWork();
        project.setId(projectId);
        return project;
    }
}
