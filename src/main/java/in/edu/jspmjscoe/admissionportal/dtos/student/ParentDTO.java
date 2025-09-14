package in.edu.jspmjscoe.admissionportal.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentDTO {

    private Long parentId;
    private Long studentId;   // we keep only studentId instead of whole Student object

    private String fatherName;
    private String motherName;
    private String fatherMobileNo;
    private String motherMobileNo;
}
