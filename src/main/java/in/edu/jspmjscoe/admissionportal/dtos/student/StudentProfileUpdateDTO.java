package in.edu.jspmjscoe.admissionportal.dtos.student;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StudentProfileUpdateDTO {
    // Address
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String state;
    private String district;
    private String taluka;
    private String village;
    private String pincode;

    // Blood group
    private String bloodGroup;

    // Parent contacts
    private String fatherMobileNo;
    private String motherMobileNo;

    // Profile picture
    private MultipartFile profilePicture;
}
