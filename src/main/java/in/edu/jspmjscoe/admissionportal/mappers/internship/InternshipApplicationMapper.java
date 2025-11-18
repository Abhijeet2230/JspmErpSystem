package in.edu.jspmjscoe.admissionportal.mappers.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipApplicationDTO;
import in.edu.jspmjscoe.admissionportal.model.internship.InternshipApplication;
import in.edu.jspmjscoe.admissionportal.model.internship.InternshipPosting;
import in.edu.jspmjscoe.admissionportal.model.internship.StudentProfile;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InternshipApplicationMapper {

    // Entity → DTO
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "student.candidateName", target = "studentName")
    @Mapping(source = "internship.internshipId", target = "internshipId")
    @Mapping(source = "internship.title", target = "internshipTitle")
    @Mapping(source = "internship.company.name", target = "companyName")
    @Mapping(source = "studentProfile.profileId", target = "profileId")
    InternshipApplicationDTO toDTO(InternshipApplication internshipApplication);

    // DTO → Entity
    @Mapping(target = "student", expression = "java(mapStudent(dto.getStudentId()))")
    @Mapping(target = "internship", expression = "java(mapInternship(dto.getInternshipId()))")
    @Mapping(target = "studentProfile", expression = "java(mapStudentProfile(dto.getProfileId()))")
    InternshipApplication toEntity(InternshipApplicationDTO dto);

    // Helper methods to create references with just IDs
    default Student mapStudent(Long studentId) {
        if (studentId == null) return null;
        Student student = new Student();
        student.setStudentId(studentId);
        return student;
    }

    default InternshipPosting mapInternship(Long internshipId) {
        if (internshipId == null) return null;
        InternshipPosting internship = new InternshipPosting();
        internship.setInternshipId(internshipId);
        return internship;
    }

    default StudentProfile mapStudentProfile(Long profileId) {
        if (profileId == null) return null;
        StudentProfile studentProfile = new StudentProfile();
        studentProfile.setProfileId(profileId);
        return studentProfile;
    }
}
