package in.edu.jspmjscoe.admissionportal.dtos.internship;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfileDTO {

    private Long profileId;
    private Long studentId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String branch;
    private String email;
    private String mobileNumber;
    private Integer currentSemester;
    private Double currentCgpa;
    private Double aggregatePercentage;
    private String deadBacklogs;
    private String liveBacklogs;
    private String clearBacklogsConfidence;
    private Double tenthPercentage;
    private String tenthBoard;
    private Double twelfthPercentage;
    private String twelfthBoard;
    private Double diplomaPercentage;
    private String diplomaBoard;
    private String careerInterest;
    private String higherStudies;
    private String placementInterest;
    private String relocationInterest;
    private String bondAcceptance;
    private String certifications;
    private String resumePath;
    private String panCard;
    private String aadhaarCard;
    private String passport;
    private String localCity;
    private String permanentCity;
    private String permanentState;
}
