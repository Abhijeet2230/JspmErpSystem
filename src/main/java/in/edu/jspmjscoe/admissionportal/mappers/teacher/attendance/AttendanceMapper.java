package in.edu.jspmjscoe.admissionportal.mappers.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceSessionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceStudentDTO;
import in.edu.jspmjscoe.admissionportal.model.teacher.attendance.AttendanceSession;
import in.edu.jspmjscoe.admissionportal.model.teacher.attendance.AttendanceStudent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(source = "subject.name", target = "subjectName")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "division", target = "division")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(target = "studentAttendances", expression = "java(toStudentDtoList(session.getStudentAttendances()))")
    AttendanceSessionDTO toSessionDto(AttendanceSession session);

    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "student.candidateName", target = "studentName")
    AttendanceStudentDTO toStudentDto(AttendanceStudent attendanceStudent);

    default List<AttendanceStudentDTO> toStudentDtoList(List<AttendanceStudent> students) {
        if (students == null) return null;
        return students.stream().map(this::toStudentDto).collect(Collectors.toList());
    }
}
