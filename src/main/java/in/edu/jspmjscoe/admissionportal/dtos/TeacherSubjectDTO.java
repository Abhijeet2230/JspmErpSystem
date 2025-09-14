package in.edu.jspmjscoe.admissionportal.dtos;

import lombok.*;

/**
 * DTO for TeacherSubject entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherSubjectDTO {

    private Long teacherSubjectId;

    // Instead of embedding full Teacher entity → just store teacherId
    private Long teacherId;
    private String teacherName; // optional convenience

    // Instead of embedding full Subject entity → just store subjectId
    private Long subjectId;
    private String subjectName; // optional convenience

    private String roleInSubject; // e.g. Instructor, Mentor, etc.
}
