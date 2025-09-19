package in.edu.jspmjscoe.admissionportal.dtos.teacher;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAddressDTO {

    private Long addressId;
    private Long teacherId;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String state;
    private String district;
    private String taluka;
    private String village;
    private String pincode;
}
