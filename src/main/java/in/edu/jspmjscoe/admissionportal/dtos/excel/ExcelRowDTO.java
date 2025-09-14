package in.edu.jspmjscoe.admissionportal.dtos.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelRowDTO {
    private String applicationId;
    private String candidateName;
    private String fatherName;
    private String motherName;
    private String gender;
    private String dob;
    private String religion;
    private String region;
    private String motherTongue;
    private String annualFamilyIncome;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String state;
    private String district;
    private String taluka;
    private String village;
    private String pincode;
    private String mobileNo;
    private String email;
    private String phoneNo;
    private String candidatureType;
    private String homeUniversity;
    private String category;
    private String phType;
    private String defenceType;
    private String linguisticMinority;
    private String religiousMinority;
    private String sscBoard;
    private String sscPassingYear;
    private String sscSeatNo;
    private Double sscMathPercentage;
    private Double sscTotalPercentage;
    private String qualifyingExam;
    private String hscBoard;
    private String hscPassingYear;
    private String hscSeatNo;
    private Double hscPhysicsPercentage;
    private Double hscChemistryPercentage;
    private Double hscMathPercentage;
    private String hscAdditionalSubject;
    private Double hscAdditionalSubjectPercentage;
    private Double hscEnglishPercentage;
    private Double hscTotalPercentage;
    private Double hscEligibilityPercentage;
    private String cetRollNo;
    private Double cetPercentile;
    private String jeeApplicationNo;
    private Double jeePercentile;
    private Integer meritNo;
    private Double meritMarks;
    private String instituteCode;
    private String instituteName;
    private String courseName;
    private String choiceCode;
    private String seatType;
    private String admissionDate;
    private String reportedDate;
}
