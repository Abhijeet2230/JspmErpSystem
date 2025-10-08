package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementApplicationDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.Placement;
import in.edu.jspmjscoe.admissionportal.model.internship.PlacementApplication;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlacementApplicationMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "student.candidateName", target = "studentName")
    @Mapping(source = "placement.placementId", target = "placementId")
    @Mapping(source = "placement.jobTitle", target = "jobTitle")
    @Mapping(source = "placement.company.name", target = "companyName")
    PlacementApplicationDTO toDTO(PlacementApplication placementApplication);

    // DTO → Entity
    @Mapping(target = "student", expression = "java(mapStudent(dto.getStudentId()))")
    @Mapping(target = "placement", expression = "java(mapPlacement(dto.getPlacementId()))")
    PlacementApplication toEntity(PlacementApplicationDTO dto);

    // Helper methods to create references with just IDs
    default Student mapStudent(Long studentId) {
        if (studentId == null) return null;
        Student student = new Student();
        student.setStudentId(studentId);
        return student;
    }

    default Placement mapPlacement(Long placementId) {
        if (placementId == null) return null;
        Placement placement = new Placement();
        placement.setPlacementId(placementId);
        return placement;
    }
}
