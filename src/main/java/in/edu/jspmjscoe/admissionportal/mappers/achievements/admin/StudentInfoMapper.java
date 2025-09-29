package in.edu.jspmjscoe.admissionportal.mappers.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.admin.BaseStudentAchievementDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentInfoMapper {

    @Mapping(target = "studentAcademicYearId", source = "studentAcademicYear.studentAcademicYearId")
    @Mapping(target = "candidateName", source = "student.candidateName")
    @Mapping(target = "rollNo", source = "rollNo")
    @Mapping(target = "division", source = "division")
    @Mapping(target = "courseName", source = "student.courseName")
    // OR use "student.course.courseName" if Course entity has a courseName column
    @Mapping(target = "yearOfStudy", source = "yearOfStudy")
    @Mapping(target = "semester", source = "semester")
    BaseStudentAchievementDTO toBaseDTO(StudentAcademicYear studentAcademicYear);
}
