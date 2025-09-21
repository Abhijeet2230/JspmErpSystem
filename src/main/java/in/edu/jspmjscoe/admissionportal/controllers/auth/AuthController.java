package in.edu.jspmjscoe.admissionportal.controllers.auth;

import in.edu.jspmjscoe.admissionportal.exception.InvalidPasswordException;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.security.jwt.JwtUtils;
import in.edu.jspmjscoe.admissionportal.exception.InvalidUsernameException;
import in.edu.jspmjscoe.admissionportal.security.response.LoginResponse;
import in.edu.jspmjscoe.admissionportal.security.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 0️⃣ Load user from DB
        User user = userRepository.findByUserName(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidUsernameException("Invalid username"));

        // 1️⃣ Extra check for teachers before authentication
        if ("ROLE_TEACHER".equals(user.getRole().getRoleName().name())) {
            Teacher teacher = teacherRepository.findByUser(user).orElse(null);

            if (teacher == null || teacher.getStatus() != Status.ACCEPTED) {
                Map<String, Object> map = new HashMap<>();
                map.put("message", "Teacher account not approved. Current status: " +
                        (teacher != null ? teacher.getStatus() : "NOT_FOUND"));
                map.put("status", false);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
            }
        }

        // 2️⃣ Check password manually
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        // 3️⃣ Authenticate (now safe, both username and password checked)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 4️⃣ Authentication successful
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (user.isFirstLogin()) user.setFirstLoginDate(LocalDateTime.now());
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        // ✅ Fetch designation only if teacher
        String designation = null;
        if (roles.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findByUser(user).orElse(null);
            if (teacher != null) {
                designation = teacher.getDesignation(); // assuming field exists in Teacher entity
            }
        }

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken, user.isFirstLogin(),designation);

        return ResponseEntity.ok(response);
    }

}

