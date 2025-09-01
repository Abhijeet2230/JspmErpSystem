package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id")
    private Long studentId;   // same as user_id

    @OneToOne
    @MapsId   // tells Hibernate to use the same PK as User
    @JoinColumn(name = "student_id")
    private User user;

    @Column(name = "application_id", unique = true)
    private String applicationId;

    @Column(name = "candidate_name")
    private String candidateName;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "dob")
    private String dob;

    @Column(name = "religion")
    private String religion;

    @Column(name = "region")
    private String region;

    @Column(name = "mother_tongue")
    private String motherTongue;

    @Column(name = "annual_family_income")
    private String annualFamilyIncome;

    @Column(name = "candidature_type")
    private String candidatureType;

    @Column(name = "home_university")
    private String homeUniversity;

    @Column(name = "category")
    private String category;

    @Column(name = "ph_type")
    private String phType;

    @Column(name = "defence_type")
    private String defenceType;

    @Column(name = "linguistic_minority")
    private String linguisticMinority;

    @Column(name = "religious_minority")
    private String religiousMinority;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Parent parent;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Address address;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private SSC ssc;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private HSC hsc;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private CET cet;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private JEE jee;

    // One student can have multiple admission records
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Admission> admissions = new ArrayList<>();


}
