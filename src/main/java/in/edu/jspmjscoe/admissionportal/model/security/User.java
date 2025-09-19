package in.edu.jspmjscoe.admissionportal.model.security;

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
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @Column(name = "first_login")
    private boolean firstLogin = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime firstLoginDate;
    private LocalDateTime lastLoginDate;

    // ✅ Relations
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Teacher teacher;

    // ✅ Proper Role mapping
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
