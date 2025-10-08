package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.GuestLectureDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.GuestLecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GuestLectureMapper {

    // Entity → DTO
    GuestLectureDTO toDTO(GuestLecture guestLecture);

    // DTO → Entity
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    GuestLecture toEntity(GuestLectureDTO dto);
    
    // Update existing entity from DTO
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    @Mapping(target = "id", ignore = true) // Don't update ID
    void updateEntityFromDto(GuestLectureDTO dto, @MappingTarget GuestLecture entity);
}
