package in.edu.jspmjscoe.admissionportal.dtos.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternshipApplicationDTO {

    // Application entity fields
    private Long applicationId;
    private Long studentId;
    private String studentName; // for display purposes
    private Long internshipId;
    private String internshipTitle; // for display purposes
    private String companyName; // for display purposes
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private LocalDate selectedDate;
    private LocalDate rejectedDate;
    private String remarks;
    
    // Application-specific fields that student fills during application
    private String coverLetter;
    private String whyInterested;
    private String expectedStipend;
    private String preferredLocation;
    
    // Personal Information (from Student entity) - for form pre-filling
    private String prnNumber;
    private String candidateName;
    
    // Personal Details (from StudentProfile) - for form pre-filling
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String branch;
    private String email;
    private String mobileNumber;
    
    // Academic Information - for form pre-filling
    private Integer currentSemester;
    private Double currentCgpa;
    private Double aggregatePercentage;
    private String deadBacklogs;
    private String liveBacklogs;
    private String clearBacklogsConfidence;
    
    // Educational Background - for form pre-filling
    private Double tenthPercentage;
    private String tenthBoard;
    private Double twelfthPercentage;
    private String twelfthBoard;
    private Double diplomaPercentage;
    private String diplomaBoard;
    
    // Career Preferences - for form pre-filling
    private String careerInterest;
    private String higherStudies;
    private String placementInterest;
    private String relocationInterest;
    private String bondAcceptance;
    
    // Skills and Documents - for form pre-filling
    private String certifications;
    private String resumePath;
    private String panCard;
    private String aadhaarCard;
    private String passport;
    
    // Address Information - for form pre-filling
    private String localCity;
    private String permanentCity;
    private String permanentState;
    
    // Validation flags - for form validation
    private Boolean hasResume;
    private Boolean profileComplete;
}
