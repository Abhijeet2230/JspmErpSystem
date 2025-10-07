package in.edu.jspmjscoe.admissionportal.mappers.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileUpdateDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.*;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentProfileMapper {

    // Entity → Response DTO
    @Mapping(target = "studentId", source = "studentId")
    @Mapping(target = "candidateName", source = "candidateName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "mobileNo", source = "mobileNo")
    @Mapping(target = "dob", source = "dob")
    @Mapping(target = "bloodGroup", source = "bloodGroup")
    @Mapping(target = "profilePicturePath", source = "profilePicturePath")

    // Address
    @Mapping(target = "addressLine1", source = "address.addressLine1")
    @Mapping(target = "addressLine2", source = "address.addressLine2")
    @Mapping(target = "addressLine3", source = "address.addressLine3")
    @Mapping(target = "state", source = "address.state")
    @Mapping(target = "district", source = "address.district")
    @Mapping(target = "taluka", source = "address.taluka")
    @Mapping(target = "village", source = "address.village")
    @Mapping(target = "pincode", source = "address.pincode")

    // Parent
    @Mapping(target = "parentContactNo", source = "parent.fatherMobileNo")

    // Academic info from active year
    @Mapping(target = "division", expression = "java(getActiveDivision(student))")
    @Mapping(target = "rollNo", expression = "java(getActiveRollNo(student))")
    StudentProfileResponseDTO toDto(Student student);

    // DTO → Entity (update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address.addressLine1", source = "addressLine1")
    @Mapping(target = "address.addressLine2", source = "addressLine2")
    @Mapping(target = "address.addressLine3", source = "addressLine3")
    @Mapping(target = "address.state", source = "state")
    @Mapping(target = "address.district", source = "district")
    @Mapping(target = "address.taluka", source = "taluka")
    @Mapping(target = "address.village", source = "village")
    @Mapping(target = "address.pincode", source = "pincode")
    @Mapping(target = "parent.fatherMobileNo", source = "parentContactNo")
    void updateStudentFromDto(StudentProfileUpdateDTO dto, @MappingTarget Student student);

    // default methods to fetch division/rollNo
    default String getActiveDivision(Student student) {
        if (student.getStudentAcademicYears() != null) {
            return student.getStudentAcademicYears().stream()
                    .filter(say -> Boolean.TRUE.equals(say.getIsActive()))
                    .findFirst()
                    .map(say -> say.getDivision())
                    .orElse(null);
        }
        return null;
    }

    default Integer getActiveRollNo(Student student) {
        if (student.getStudentAcademicYears() != null) {
            return student.getStudentAcademicYears().stream()
                    .filter(say -> Boolean.TRUE.equals(say.getIsActive()))
                    .findFirst()
                    .map(say -> say.getRollNo())
                    .orElse(null);
        }
        return null;
    }
}
