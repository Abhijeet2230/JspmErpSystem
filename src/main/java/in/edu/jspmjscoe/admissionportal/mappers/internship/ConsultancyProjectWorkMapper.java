package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.ConsultancyProjectWorkDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.ConsultancyProjectWork;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultancyProjectWorkMapper {

    // Entity → DTO
    @Mapping(source = "referredBy.userId", target = "referredByUserId")
    @Mapping(target = "referredByUsername", expression = "java(getReferredByName(consultancyProjectWork.getReferredBy()))")
    ConsultancyProjectWorkDTO toDTO(ConsultancyProjectWork consultancyProjectWork);

    // DTO → Entity
    @Mapping(target = "referredBy", expression = "java(mapUser(dto.getReferredByUserId()))")
    @Mapping(target = "students", ignore = true) // Ignore ManyToMany relationship
    @Mapping(target = "applications", ignore = true) // Ignore bidirectional relationship
    ConsultancyProjectWork toEntity(ConsultancyProjectWorkDTO dto);
    
    // Helper method to create User reference with just ID
    default in.edu.jspmjscoe.admissionportal.model.security.User mapUser(Long userId) {
        if (userId == null) return null;
        in.edu.jspmjscoe.admissionportal.model.security.User user = new in.edu.jspmjscoe.admissionportal.model.security.User();
        user.setUserId(userId);
        return user;
    }
    
    // Helper method to get the full name of the referred by user
    default String getReferredByName(in.edu.jspmjscoe.admissionportal.model.security.User user) {
        if (user == null) return null;
        
        // Check if user has a teacher profile
        if (user.getTeacher() != null) {
            var teacher = user.getTeacher();
            StringBuilder name = new StringBuilder();
            if (teacher.getPrefix() != null) name.append(teacher.getPrefix()).append(" ");
            if (teacher.getFirstName() != null) name.append(teacher.getFirstName()).append(" ");
            if (teacher.getMiddleName() != null) name.append(teacher.getMiddleName()).append(" ");
            if (teacher.getLastName() != null) name.append(teacher.getLastName());
            String fullName = name.toString().trim();
            return fullName.isEmpty() ? user.getUserName() : fullName;
        }
        
        // Check if user has a student profile
        if (user.getStudent() != null) {
            var student = user.getStudent();
            if (student.getCandidateName() != null && !student.getCandidateName().isEmpty()) {
                return student.getCandidateName();
            }
        }
        
        // Fallback to username if no profile exists
        return user.getUserName();
    }
}
