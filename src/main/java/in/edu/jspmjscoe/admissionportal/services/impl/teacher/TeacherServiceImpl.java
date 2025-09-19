package in.edu.jspmjscoe.admissionportal.services.impl.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.HeadLeaveMapper;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.LeaveMapper;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.TeacherMapper;
import in.edu.jspmjscoe.admissionportal.model.security.AppRole;
import in.edu.jspmjscoe.admissionportal.model.security.Role;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.teacher.HeadLeave;
import in.edu.jspmjscoe.admissionportal.model.teacher.Leave;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.repositories.security.RoleRepository;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.DepartmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.HeadLeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.LeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveMapper leaveMapper;
    private final HeadLeaveRepository headLeaveRepository;
    private final HeadLeaveMapper headLeaveMapper;


    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherDTO getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(TeacherMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
    }

    @Override
    public TeacherDTO saveTeacher(TeacherDTO teacherDTO) {
        // ✅ Fetch or create User
        User user;
        if (teacherDTO.getUserId() != null) {
            user = userRepository.findById(teacherDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + teacherDTO.getUserId()));
        } else {
            // 🔹 Auto-create User
            user = new User();

            if (teacherDTO.getPersonalEmail() == null || teacherDTO.getPersonalEmail().isBlank()) {
                throw new RuntimeException("Personal email is required to create a user");
            }
            user.setUserName(teacherDTO.getPersonalEmail());

            // 👇 Default password is DOB
            user.setPassword(passwordEncoder.encode(teacherDTO.getDateOfBirth()));

            // 👇 Ensure ROLE_TEACHER exists or create it
            Role teacherRole = roleRepository.findByRoleName(AppRole.ROLE_TEACHER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_TEACHER)));

            user.setRole(teacherRole);
            user = userRepository.save(user);
        }

        // ✅ Fetch department
        Department department = teacherDTO.getDepartmentId() != null ?
                departmentRepository.findById(teacherDTO.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found with id: " + teacherDTO.getDepartmentId()))
                : null;

        // ✅ Map DTO → Entity
        Teacher teacher = TeacherMapper.toEntity(teacherDTO, user, department);

        // 🔹 New teachers are always "PENDING"
        teacher.setStatus(Status.PENDING);

        // ✅ Save entity
        Teacher savedTeacher = teacherRepository.save(teacher);

        return TeacherMapper.toDTO(savedTeacher);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        String username = authentication.getName(); // personalEmail
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return user.getUserId();
    }

    // ✅ Get logged-in teacher and enforce "ACCEPTED" status
    public TeacherDTO getCurrentTeacherProfile() {
        Long userId = getCurrentUserId();
        Teacher teacher = teacherRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        if (teacher.getStatus() != Status.ACCEPTED) {
            throw new RuntimeException("Teacher account is not yet approved");
        }

        return TeacherMapper.toDTO(teacher);
    }
    @Override
    public TeacherDTO updateTeacherStatus(Long id, Status status) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        if (status == Status.REJECTED) {
            // delete teacher and return DTO before deletion
            TeacherDTO teacherDTO = TeacherMapper.toDTO(teacher);

            // First delete the teacher (also consider deleting user if linked)
            teacherRepository.delete(teacher);

            return teacherDTO; // return last known details
        }

        // Otherwise (for ACCEPTED or other statuses), just update
        teacher.setStatus(status);
        Teacher updatedTeacher = teacherRepository.save(teacher);
        return TeacherMapper.toDTO(updatedTeacher);
    }


    @Override
    public List<TeacherDTO> getAcceptedTeachers() {
        return teacherRepository.findByStatus(Status.ACCEPTED)
                .stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherDTO> getPendingTeachers() {
        return teacherRepository.findByStatus(Status.PENDING)
                .stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --------------- Teacher Leave -------------//
    @Override
    public LeaveDTO applyLeave(LeaveDTO leaveDTO) {
        // 🔹 Fetch currently logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new RuntimeException("Unauthorized");
        }
        UserDetails userDetails = (UserDetails) principal;

        // 🔹 Get User entity
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔹 Get Teacher linked to the logged-in user
        Teacher teacher = teacherRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // ❌ Check if teacher already has a pending leave
        boolean hasPendingLeave = leaveRepository
                .existsByTeacher_TeacherIdAndStatus(teacher.getTeacherId(), Status.PENDING);

        if (hasPendingLeave) {
            throw new RuntimeException("You already have a pending leave request. Wait until it is approved/rejected.");
        }

        // ✅ Map DTO to entity
        Leave leave = leaveMapper.toEntity(leaveDTO, teacher);

        // Set default status
        leave.setStatus(Status.PENDING);

        Leave savedLeave = leaveRepository.save(leave);
        return leaveMapper.toDTO(savedLeave);
    }

    @Override
    public List<LeaveDTO> getPendingLeaves() {
        List<Leave> leaves = leaveRepository.findByStatus(Status.PENDING);
        if (leaves.isEmpty()) {
            throw new RuntimeException("No pending leaves found");
        }

        return leaves.stream()
                .map(leaveMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveDTO> getAcceptedLeaves() {
        return leaveRepository.findByStatus(Status.ACCEPTED)
                .stream()
                .map(leaveMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveDTO updateLeaveStatus(Long id, Status status) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with id: " + id));

        leave.setStatus(status);
        Leave updatedLeave = leaveRepository.save(leave);

        return leaveMapper.toDTO(updatedLeave);
    }

    // --------------- Head Leave -------------//
    @Override
    public HeadLeaveDTO applyHeadLeave(HeadLeaveDTO headLeaveDTO) {
        // 🔹 Get logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new RuntimeException("Unauthorized");
        }
        UserDetails userDetails = (UserDetails) principal;

        // 🔹 Find User entity
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔹 Find Teacher entity
        Teacher teacher = teacherRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // ❌ Check if already pending
        boolean hasPendingLeave = headLeaveRepository
                .existsByTeacher_TeacherIdAndStatus(teacher.getTeacherId(), Status.PENDING);

        if (hasPendingLeave) {
            throw new RuntimeException("You already have a pending head leave request.");
        }

        // ✅ Map DTO → Entity
        HeadLeave headLeave = headLeaveMapper.toEntity(headLeaveDTO, teacher);
        headLeave.setStatus(Status.PENDING);

        HeadLeave saved = headLeaveRepository.save(headLeave);
        return headLeaveMapper.toDTO(saved);
    }

    @Override
    public List<HeadLeaveDTO> getPendingHeadLeaves() {
        return headLeaveRepository.findByStatus(Status.PENDING)
                .stream()
                .map(headLeaveMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HeadLeaveDTO> getAcceptedHeadLeaves() {
        return headLeaveRepository.findByStatus(Status.ACCEPTED)
                .stream()
                .map(headLeaveMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HeadLeaveDTO updateHeadLeaveStatus(Long id, Status status) {
        HeadLeave headLeave = headLeaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Head Leave not found with id: " + id));

        headLeave.setStatus(status);
        HeadLeave updated = headLeaveRepository.save(headLeave);

        return headLeaveMapper.toDTO(updated);
    }

}
