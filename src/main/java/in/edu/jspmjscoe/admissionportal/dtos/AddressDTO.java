package in.edu.jspmjscoe.admissionportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private Long addressId;
    private Long studentId;   // only FK, not full Student entity

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String state;
    private String district;
    private String taluka;
    private String village;
    private String pincode;
}
