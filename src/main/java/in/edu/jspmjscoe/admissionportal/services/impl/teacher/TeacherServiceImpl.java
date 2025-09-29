package in.edu.jspmjscoe.admissionportal.services.impl.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherSubjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import in.edu.jspmjscoe.admissionportal.exception.*;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.HeadLeaveMapper;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.LeaveMapper;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.TeacherMapper;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.appriasal.TeacherAppraisalMapper;
import in.edu.jspmjscoe.admissionportal.model.security.AppRole;
import in.edu.jspmjscoe.admissionportal.model.security.Role;
import in.edu.jspmjscoe.admissionportal.model.security.Status;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.teacher.HeadLeave;
import in.edu.jspmjscoe.admissionportal.model.teacher.Leave;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherSubject;
import in.edu.jspmjscoe.admissionportal.model.teacher.appriasal.TeacherAppraisal;
import in.edu.jspmjscoe.admissionportal.repositories.security.RoleRepository;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.DepartmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.SubjectRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.HeadLeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.LeaveRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.appriasal.TeacherAppraisalRepository;
import in.edu.jspmjscoe.admissionportal.security.services.CurrentUserService;
import in.edu.jspmjscoe.admissionportal.services.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveMapper leaveMapper;
    private final HeadLeaveRepository headLeaveRepository;
    private final HeadLeaveMapper headLeaveMapper;
    private final CurrentUserService currentUserService;
    private final TeacherAppraisalMapper teacherAppraisalMapper;
    private final TeacherAppraisalRepository teacherAppraisalRepository;


    // --------------------- TEACHER CRUD ---------------------
    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherDTO getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(TeacherMapper::toDTO)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with ID: " + id));
    }

    @Override
    public TeacherDTO saveTeacher(TeacherDTO teacherDTO) {
        // 1️⃣ Fetch or create User
        User user;
        if (teacherDTO.getUserId() != null) {
            user = userRepository.findById(teacherDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + teacherDTO.getUserId()));
        } else {
            user = new User();
            if (teacherDTO.getPersonalEmail() == null || teacherDTO.getPersonalEmail().isBlank()) {
                throw new IllegalArgumentException("Personal email is required to create a user");
            }
            user.setUserName(teacherDTO.getPersonalEmail());
            user.setPassword(passwordEncoder.encode(teacherDTO.getDateOfBirth()));

            Role teacherRole = roleRepository.findByRoleName(AppRole.ROLE_TEACHER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_TEACHER)));
            user.setRole(teacherRole);
            user = userRepository.save(user);
        }

        // 2️⃣ Fetch department
        Department department = null;
        if (teacherDTO.getDepartmentId() != null) {
            department = departmentRepository.findById(teacherDTO.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + teacherDTO.getDepartmentId()));
        }

        // 3️⃣ Map DTO → Teacher entity
        Teacher teacher = TeacherMapper.toEntity(teacherDTO, user, department);

        // 4️⃣ Set default status
        teacher.setStatus(Status.PENDING);

        // 6️⃣ Save teacher with teacherSubjects
        Teacher savedTeacher = teacherRepository.save(teacher);
        return TeacherMapper.toDTO(savedTeacher);
    }

    // --------------------- CURRENT USER HELPERS ---------------------
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        String username = authentication.getName(); // personalEmail
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        return user.getUserId();
    }

    public TeacherDTO getCurrentTeacherProfile() {
        Long userId = getCurrentUserId();
        Teacher teacher = teacherRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found for User ID: " + userId));

        if (teacher.getStatus() != Status.ACCEPTED) {
            throw new TeacherAccountNotApprovedException("Teacher account is not yet approved");
        }

        return TeacherMapper.toDTO(teacher);
    }

    @Override
    public TeacherDTO updateTeacherStatus(Long id, Status status) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with ID: " + id));

        if (status == Status.REJECTED) {
            TeacherDTO dto = TeacherMapper.toDTO(teacher);
            teacherRepository.delete(teacher);
            return dto;
        }

        teacher.setStatus(status);
        Teacher updatedTeacher = teacherRepository.save(teacher);
        return TeacherMapper.toDTO(updatedTeacher);
    }

    @Override
    public List<TeacherDTO> getAcceptedTeachers() {
        return teacherRepository.findByStatus(Status.ACCEPTED).stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherDTO> getPendingTeachers() {
        return teacherRepository.findByStatus(Status.PENDING).stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --------------------- Assign Teacher ---------------------

    // TeacherServiceImpl.java
    @Override
    public TeacherSubjectDTO assignSubjectToTeacher(Long teacherId, Long subjectId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with ID: " + teacherId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with ID: " + subjectId));

        // check duplicate
        boolean alreadyAssigned = teacher.getTeacherSubjects().stream()
                .anyMatch(ts -> ts.getSubject().getSubjectId().equals(subjectId));
        if (alreadyAssigned) {
            throw new IllegalStateException("Teacher already assigned to this subject");
        }

        TeacherSubject ts = TeacherSubject.builder()
                .teacher(teacher)
                .subject(subject)
                .build();

        teacher.addTeacherSubject(ts);  // maintain relationship
        subject.addTeacherSubject(ts);

        teacherRepository.save(teacher); // cascades TeacherSubject

        return TeacherSubjectDTO.builder()
                .teacherSubjectId(ts.getTeacherSubjectId())
                .teacherId(teacher.getTeacherId())
                .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                .subjectId(subject.getSubjectId())
                .subjectName(subject.getName())
                .build();
    }

    // --------------------- TEACHER LEAVES ---------------------
    @Override
    public LeaveDTO applyLeave(LeaveDTO leaveDTO) {
        Teacher teacher = currentUserService.getCurrentTeacher();

        boolean hasPendingLeave = leaveRepository
                .existsByTeacher_TeacherIdAndStatus(teacher.getTeacherId(), Status.PENDING);

        if (hasPendingLeave) {
            throw new PendingLeaveExistsException("Teacher already has a pending leave");
        }

        Leave leave = leaveMapper.toEntity(leaveDTO, teacher);
        leave.setStatus(Status.PENDING);

        Leave savedLeave = leaveRepository.save(leave);
        return leaveMapper.toDTO(savedLeave);
    }

    @Transactional
    @Override
    public LeaveDTO endLeaveEarly(Long leaveId, LocalTime actualCloserTime) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

        if (leave.getStatus() != Status.ACCEPTED) {
            throw new IllegalStateException("Leave is not approved yet");
        }

        // only update if teacher returned earlier
        if (actualCloserTime.isBefore(leave.getCloserTime())) {
            leave.setActualCloserTime(actualCloserTime);

            // calculate hours difference
            long hours = Duration.between(leave.getLeaveTime(), actualCloserTime).toHours();
            leave.setLeaveHours((double) hours);

            leaveRepository.save(leave);
        }

        return leaveMapper.toDTO(leave);
    }

    @Override
    public List<LeaveDTO> getPendingLeaves() {
        List<Leave> leaves = leaveRepository.findByStatus(Status.PENDING);
        if (leaves.isEmpty()) throw new NoPendingLeavesException("No pending leaves found");
        return leaves.stream().map(leaveMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<LeaveDTO> getAcceptedLeaves() {
        List<Leave> leaves = leaveRepository.findByStatus(Status.ACCEPTED);
        if (leaves.isEmpty()) throw new NoPendingLeavesException("No accepted leaves found");
        return leaves.stream().map(leaveMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public LeaveDTO updateLeaveStatus(Long id, Status status) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found with ID: " + id));
        leave.setStatus(status);
        return leaveMapper.toDTO(leaveRepository.save(leave));
    }

    // --------------------- HEAD LEAVES ---------------------
    @Override
    public HeadLeaveDTO applyHeadLeave(HeadLeaveDTO headLeaveDTO) {
        Teacher teacher = currentUserService.getCurrentTeacher();
        boolean hasPendingLeave = headLeaveRepository.existsByTeacher_TeacherIdAndStatus(teacher.getTeacherId(), Status.PENDING);
        if (hasPendingLeave) throw new PendingHeadLeaveExistsException("Teacher already has a pending head leave");

        HeadLeave headLeave = headLeaveMapper.toEntity(headLeaveDTO, teacher);
        headLeave.setStatus(Status.PENDING);
        return headLeaveMapper.toDTO(headLeaveRepository.save(headLeave));
    }

    @Transactional
    @Override
    public HeadLeaveDTO endHeadLeaveEarly(Long headLeaveId, LocalDate actualToDate) {
        HeadLeave leave = headLeaveRepository.findById(headLeaveId)
                .orElseThrow(() -> new ResourceNotFoundException("HeadLeave not found"));

        if (leave.getStatus() != Status.ACCEPTED) {
            throw new IllegalStateException("Leave is not approved yet");
        }

        // only update if teacher returned earlier
        if (actualToDate.isBefore(leave.getToDate())) {
            leave.setActualToDate(actualToDate);

            int newDays = (int) ChronoUnit.DAYS.between(leave.getFromDate(), actualToDate) + 1;
            leave.setNumberOfDays(newDays);

            headLeaveRepository.save(leave);
        }

        return headLeaveMapper.toDTO(leave);
    }

    @Override
    public List<HeadLeaveDTO> getPendingHeadLeaves() {
        List<HeadLeave> leaves = headLeaveRepository.findByStatus(Status.PENDING);
        if (leaves.isEmpty()) throw new NoPendingLeavesException("No pending head leaves found");
        return leaves.stream().map(headLeaveMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<HeadLeaveDTO> getAcceptedHeadLeaves() {
        List<HeadLeave> leaves = headLeaveRepository.findByStatus(Status.ACCEPTED);
        if (leaves.isEmpty()) throw new NoPendingLeavesException("No accepted head leaves found");
        return leaves.stream().map(headLeaveMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public HeadLeaveDTO updateHeadLeaveStatus(Long id, Status status) {
        HeadLeave headLeave = headLeaveRepository.findById(id)
                .orElseThrow(() -> new HeadLeaveNotFoundException("Head leave not found with ID: " + id));
        headLeave.setStatus(status);
        return headLeaveMapper.toDTO(headLeaveRepository.save(headLeave));
    }

    //------------------- TeacherAppriasal --------------------//
    @Override
    public TeacherAppraisalDTO createAppraisal(TeacherAppraisalDTO dto) {
        TeacherAppraisal entity = teacherAppraisalMapper.toEntity(dto);
        TeacherAppraisal saved = teacherAppraisalRepository.save(entity);
        return teacherAppraisalMapper.toDTO(saved);
    }

    @Override
    public TeacherAppraisalDTO getAppraisalById(Long id) {
        return teacherAppraisalRepository.findById(id)
                .map(teacherAppraisalMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Appraisal not found with id " + id));
    }

    @Override
    public List<TeacherAppraisalDTO> getAppraisalsByTeacher(Long teacherId) {
        return teacherAppraisalRepository.findByTeacher_TeacherId(teacherId)
                .stream().map(teacherAppraisalMapper::toDTO).toList();
    }

    @Override
    public List<TeacherAppraisalDTO> getAllAppraisals() {
        return teacherAppraisalRepository.findAll()
                .stream().map(teacherAppraisalMapper::toDTO).toList();
    }
}
