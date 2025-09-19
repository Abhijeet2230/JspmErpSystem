package in.edu.jspmjscoe.admissionportal.model.security;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true, name = "username",length = 100, nullable = false)
    private String userName;

    @Size(max = 120)
    @Column(name = "password")
    @JsonIgnore
    private String password;

    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @JsonBackReference
    @ToString.Exclude
    private Role role;

    @Column(name = "first_login")
    private boolean firstLogin = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;   // time of import

    private LocalDateTime firstLoginDate;  // when student logs in first time
    private LocalDateTime lastLoginDate;

    public User(String userName, String password) {
        this.userName=userName;
        this.password=password;
    }

    // One-to-One with Student (shared PK)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Student student;

    // One-to-One with Teacher (shared PK)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Teacher teacher;

}
