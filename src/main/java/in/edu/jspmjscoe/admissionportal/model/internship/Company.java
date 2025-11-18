package in.edu.jspmjscoe.admissionportal.model.internship;

import in.edu.jspmjscoe.admissionportal.model.converters.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "website", columnDefinition = "TEXT")
    private String website;

    @Column(name = "year_established")
    private Integer yearEstablished;

    @Column(name = "hr_name")
    private String hrName;

    @Column(name = "industry_type")
    private String industryType;

    @Column(name = "parent_group", columnDefinition = "TEXT")
    private String parentGroup;

    @Column(name = "employee_strength")
    private Integer employeeStrength;

    @Column(name = "contact_email", columnDefinition = "TEXT")
    private String contactEmail;

    @Column(name = "contact_mobile", columnDefinition = "TEXT")
    private String contactMobile;

    @Column(name = "profiles_offered", columnDefinition = "TEXT")
    private String profilesOffered;

    @Builder.Default
    @Convert(converter = StringListConverter.class)
    @Column(name = "eligible_branches", columnDefinition = "TEXT")
    private List<String> eligibleBranches = new ArrayList<>();

    @Column(name = "default_vacancies")
    private Integer defaultVacancies;

    @Column(name = "default_ctc", precision = 12, scale = 2)
    private BigDecimal defaultCtc;

    @Column(name = "default_bond_required")
    private String defaultBondRequired;

    @Column(name = "hiring_process", columnDefinition = "TEXT")
    private String hiringProcess;

    @Column(name = "agreement_required")
    private String agreementRequired;

    @Column(name = "hiring_status")
    private String hiringStatus;
}
