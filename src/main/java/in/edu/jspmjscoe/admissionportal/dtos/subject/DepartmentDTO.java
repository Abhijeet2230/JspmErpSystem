package in.edu.jspmjscoe.admissionportal.dtos.subject;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {
    private Long departmentId;
    private String name;
    private String code;

    // Instead of exposing full objects, just IDs for lightweight DTO
    private List<CourseDTO> courses;
    private List<TeacherDTO> teachers;
}
