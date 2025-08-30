package in.edu.jspmjscoe.admissionportal.security;

import in.edu.jspmjscoe.admissionportal.model.AppRole;
import in.edu.jspmjscoe.admissionportal.model.Role;
import in.edu.jspmjscoe.admissionportal.model.User;
import in.edu.jspmjscoe.admissionportal.repository.RoleRepository;
import in.edu.jspmjscoe.admissionportal.repository.UserRepository;
import in.edu.jspmjscoe.admissionportal.security.jwt.AuthEntryPointJwt;
import in.edu.jspmjscoe.admissionportal.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/auth/public/**"));
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/auth/public/**", "/api/csrf-token").permitAll()
                .anyRequest().authenticated());
        http.exceptionHandling(exception
                -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.formLogin(withDefaults());
//        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if not present
            Role studentRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT).orElseGet(()
                    -> roleRepository.save(new Role(AppRole.ROLE_STUDENT)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseGet(()
                    -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User();
                user1.setUserName("user1");  // ✅ required
                user1.setPassword(passwordEncoder.encode("password1"));
                user1.setRole(studentRole);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User();
                admin.setUserName("admin");  // ✅ required
                admin.setPassword(passwordEncoder.encode("adminPass"));
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        };
    }
}
