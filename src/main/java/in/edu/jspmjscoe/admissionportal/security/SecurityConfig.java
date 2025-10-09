package in.edu.jspmjscoe.admissionportal.security;

import in.edu.jspmjscoe.admissionportal.model.security.AppRole;
import in.edu.jspmjscoe.admissionportal.model.security.Role;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.repositories.security.RoleRepository;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.security.jwt.AuthEntryPointJwt;
import in.edu.jspmjscoe.admissionportal.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;


    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;  // <-- inject existing component


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//       http.csrf(csrf -> csrf
//               .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .ignoringRequestMatchers("/api/auth/public/**"));

        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/admin/cce/**").hasAnyRole("ADMIN","TEACHER")
                .requestMatchers("/api/admin/training-placement/**").hasAnyRole("ADMIN","TEACHER")
                .requestMatchers("/api/admin/achievements/**").hasAnyRole("ADMIN","TEACHER")
                .requestMatchers("/api/admin/**","/actuator/**").hasRole("ADMIN")
                .requestMatchers("/api/student/**").hasRole("STUDENT")
                .requestMatchers("/api/teacher/**").hasRole("TEACHER")
                .requestMatchers("/api/files/**").permitAll()
                .requestMatchers(
                        "/api/auth/public/**",
                        "/api/csrf-token",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/api-docs",                 // 👈 new path for your JSON docs
                        "/api-docs/**"               // 👈 in case of nested
                ).permitAll()
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/assets/**",          // <—— add this for Vite build assets
                        "/*.js",               // main JS bundles
                        "/*.css",              // if your build outputs root CSS
                        "/favicon.ico",
                        "/manifest.json",
                        "/vite.svg"
                        ).permitAll()
                .anyRequest().authenticated());
        http.exceptionHandling(exception
                -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // ✅ allow iframe from same origin (your frontend + backend are usually same host in prod)

        http.headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
                .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self' " + frontendUrl))
        );

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(withDefaults());
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));  // use field here
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

            Role teacherRole = roleRepository.findByRoleName(AppRole.ROLE_TEACHER).orElseGet(()
                    -> roleRepository.save(new Role(AppRole.ROLE_TEACHER)));


            if (!userRepository.existsByUserName("admin1")) {
                User admin = new User();
                admin.setUserName("admin1");  // ✅ required
                admin.setPassword(passwordEncoder.encode("adminPass"));
                admin.setRole(adminRole);
                admin.setFirstLogin(false);
                userRepository.save(admin);
            }

            // Create default teacher
            if (!userRepository.existsByUserName("teacher")) {
                User teacher = new User();
                teacher.setUserName("teacher");
                teacher.setPassword(passwordEncoder.encode("teacherPass"));
                teacher.setRole(teacherRole);
                userRepository.save(teacher);
            }
        };
    }
}
