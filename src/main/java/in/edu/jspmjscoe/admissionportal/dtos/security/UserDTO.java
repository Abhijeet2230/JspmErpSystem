package in.edu.jspmjscoe.admissionportal.dtos.security;

import in.edu.jspmjscoe.admissionportal.model.security.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String userName;
    private Role role;  // from Role entity

    private boolean enabled;
    private boolean firstLogin;

    private LocalDateTime createdDate;
    private LocalDateTime firstLoginDate;
    private LocalDateTime lastLoginDate;

    private Long studentId;   // ✅ from Student
    private Long teacherId;   // ✅ from Teacher
}
