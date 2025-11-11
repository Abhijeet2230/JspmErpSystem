package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipPostingDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.Company;
import in.edu.jspmjscoe.admissionportal.model.internship.InternshipPosting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InternshipPostingMapper {

    // Entity → DTO
    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "referredBy.userId", target = "referredByUserId")
    @Mapping(target = "referredByUsername", expression = "java(getReferredByName(internshipPosting.getReferredBy()))")
    InternshipPostingDTO toDTO(InternshipPosting internshipPosting);

    // DTO → Entity
    @Mapping(target = "company", expression = "java(mapCompany(dto.getCompanyId()))")
    @Mapping(target = "referredBy", expression = "java(mapUser(dto.getReferredByUserId()))")
    @Mapping(target = "applications", ignore = true) // Ignore bidirectional relationship
    InternshipPosting toEntity(InternshipPostingDTO dto);
    
    // Update existing entity from DTO
    @Mapping(target = "company", ignore = true) // Don't update company relationship
    @Mapping(target = "referredBy", ignore = true) // Don't update referredBy relationship
    @Mapping(target = "applications", ignore = true) // Ignore bidirectional relationship
    @Mapping(target = "internshipId", ignore = true) // Don't update ID
    void updateEntityFromDto(InternshipPostingDTO dto, @MappingTarget InternshipPosting entity);

    // Helper method to create Company reference with just ID
    default Company mapCompany(Long companyId) {
        if (companyId == null) return null;
        Company company = new Company();
        company.setCompanyId(companyId);
        return company;
    }
    
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
