package in.edu.jspmjscoe.admissionportal.controllers;

import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.security.jwt.JwtUtils;
import in.edu.jspmjscoe.admissionportal.security.response.LoginResponse;
import in.edu.jspmjscoe.admissionportal.security.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 0️⃣ Load user from DB
        User user = userRepository.findByUserName(loginRequest.getUsername()).orElse(null);

        // 1️⃣ Extra check for teachers before authentication
        if (user != null && "ROLE_TEACHER".equals(user.getRole().getRoleName().name())) {
            Teacher teacher = teacherRepository.findByUser(user).orElse(null);

            if (teacher == null || teacher.getStatus() != Status.ACCEPTED) {
                Map<String, Object> map = new HashMap<>();
                map.put("message", "Teacher account not approved. Current status: " +
                        (teacher != null ? teacher.getStatus() : "NOT_FOUND"));
                map.put("status", false);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
            }
        }

        // 2️⃣ Authenticate username/password
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }

        // 3️⃣ Authentication successful, set context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 4️⃣ First login & last login update
        if (user.isFirstLogin()) user.setFirstLoginDate(LocalDateTime.now());
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);

        // 5️⃣ Collect roles & generate JWT
        List<String> roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken, user.isFirstLogin());

        return ResponseEntity.ok(response);
    }
}

