package in.edu.jspmjscoe.admissionportal.dtos.student;

import in.edu.jspmjscoe.admissionportal.model.student.BloodGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfileResponseDTO {
    private Long studentId;
    private String candidateName;  // for display only
    private String email;
    private String mobileNo;
    private String dob;
    private BloodGroup bloodGroup;
    private String profilePicturePath;

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String state;
    private String district;
    private String taluka;
    private String village;
    private String pincode;

    private String parentContactNo;

    // New fields
    private String division;
    private Integer rollNo;
}
