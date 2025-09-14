package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @OneToOne
    @JoinColumn(name = "teacher_id", nullable = false, unique = true)
    @JsonBackReference
    private Teacher teacher;   // FK → Teacher.teacher_id

    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "address_line3")
    private String addressLine3;

    @Column(name = "state")
    private String state;

    @Column(name = "district")
    private String district;

    @Column(name = "taluka")
    private String taluka;

    @Column(name = "village")
    private String village;

    @Column(name = "pincode")
    private String pincode;
}
