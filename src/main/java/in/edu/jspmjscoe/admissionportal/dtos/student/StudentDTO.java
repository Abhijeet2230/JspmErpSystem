package in.edu.jspmjscoe.admissionportal.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    private Long studentId;
    private Long userId;   // ✅ Added for mapping with User

    private String applicationId;
    private String candidateName;
    private String mobileNo;
    private String email;
    private String gender;
    private Integer rollNo;
    private String division;
    private String dob;
    private String religion;
    private String region;
    private String motherTongue;
    private String annualFamilyIncome;
    private String candidatureType;
    private String homeUniversity;
    private String category;
    private String phType;
    private String defenceType;
    private String linguisticMinority;
    private String religiousMinority;

    // Child DTOs instead of Entities
    private ParentDTO parent;
    private AddressDTO address;
    private SSCDTO ssc;
    private HSCDTO hsc;
    private List<EntranceExamDTO> entranceExams;

    // Admission history
    private List<AdmissionDTO> admissions;
}
