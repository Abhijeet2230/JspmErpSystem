package in.edu.jspmjscoe.admissionportal.security.services;

import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeacherRepository teacherRepository; // ✅ Inject teacher repo

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // ✅ Extra check for TEACHER role
        if (user.getRole().getRoleName().name().equals("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Teacher profile not found for user: " + username));

            if (teacher.getStatus() != Status.ACCEPTED) {
                throw new RuntimeException("Teacher account not approved. Current status: " + teacher.getStatus());
            }
        }

        return UserDetailsImpl.build(user);
    }
}
