package in.edu.jspmjscoe.admissionportal.security;

import in.edu.jspmjscoe.admissionportal.model.AppRole;
import in.edu.jspmjscoe.admissionportal.model.Role;
import in.edu.jspmjscoe.admissionportal.model.User;
import in.edu.jspmjscoe.admissionportal.repository.RoleRepository;
import in.edu.jspmjscoe.admissionportal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            // Create roles if not present
            Role studentRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_STUDENT)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User();
                user1.setUserName("user1");  // ✅ required
                user1.setPassword("{noop}password1");
                user1.setRole(studentRole);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User();
                admin.setUserName("admin");  // ✅ required
                admin.setPassword("{noop}adminPass");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        };
    }
}
