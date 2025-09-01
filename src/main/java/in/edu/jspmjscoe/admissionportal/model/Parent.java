package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private Long parentId;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @JsonBackReference
    private Student student;   // FK → Student.student_id (strict 1:1)

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "father_mobile_no")
    private String fatherMobileNo;

    @Column(name = "mother_mobile_no")
    private String motherMobileNo;
}
