package in.edu.jspmjscoe.admissionportal.dtos.excel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelStudentDTO {
    private String candidateName;
    private String dob;
    private String courseName;
    private Integer rollNo;
    private String division;
}
