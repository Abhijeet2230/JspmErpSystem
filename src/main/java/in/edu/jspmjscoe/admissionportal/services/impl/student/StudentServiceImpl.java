package in.edu.jspmjscoe.admissionportal.services.impl.student;

import in.edu.jspmjscoe.admissionportal.dtos.security.ChangePasswordRequest;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.StudentProfileUpdateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentOverallSubjectAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.exception.security.InvalidCredentialsException;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.student.StudentMapper;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.student.Address;
import in.edu.jspmjscoe.admissionportal.model.student.BloodGroup;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.teacher.attendance.AttendanceSession;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.attendance.AttendanceSessionRepository;
import in.edu.jspmjscoe.admissionportal.services.impl.minio.MinioStorageService;
import in.edu.jspmjscoe.admissionportal.services.student.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttendanceSessionRepository  attendanceSessionRepository;
    private final MinioStorageService minioStorageService;


    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }


    public void changePassword(String username, ChangePasswordRequest request) {
        // Load user
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        // Set new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Flip first login flag
        if (user.isFirstLogin()) {
            user.setFirstLogin(false);
        }

        userRepository.save(user);

    }

    // ---------------------- Student Overall Attendance per subject ------------//
    @Override
    public List<StudentOverallSubjectAttendanceDTO> getOverallAttendanceForStudent(String username) {
        // 🔹 Get the logged-in student by username
        Student student = studentRepository.findAll().stream()
                .filter(s -> s.getUser().getUserName().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String division = student.getStudentAcademicYears().stream()
                .filter(ay -> ay.getIsActive())
                .findFirst()
                .map(ay -> ay.getDivision())
                .orElseThrow(() -> new RuntimeException("Division not found for student"));

        // 🔹 Get all sessions for this student's division
        List<AttendanceSession> sessions = attendanceSessionRepository.findAll().stream()
                .filter(s -> s.getDivision().equalsIgnoreCase(division))
                .toList();

        if (sessions.isEmpty()) {
            return Collections.emptyList();
        }

        // 🔹 Group sessions by subject
        Map<Long, List<AttendanceSession>> sessionsBySubject = sessions.stream()
                .collect(Collectors.groupingBy(s -> s.getSubject().getSubjectId()));

        List<StudentOverallSubjectAttendanceDTO> result = new ArrayList<>();

        for (Map.Entry<Long, List<AttendanceSession>> entry : sessionsBySubject.entrySet()) {
            Long subjectId = entry.getKey();
            List<AttendanceSession> subjectSessions = entry.getValue();

            int totalSessions = subjectSessions.size();
            int presentCount = (int) subjectSessions.stream()
                    .map(session ->
                            session.getStudentAttendances().stream()
                                    .filter(sa -> sa.getStudent().getStudentId().equals(student.getStudentId()))
                                    .findFirst()
                                    .map(sa -> sa.getStatus().equalsIgnoreCase("present") ? 1 : 0)
                                    .orElse(0))
                    .reduce(0, Integer::sum);

            double percentage = totalSessions == 0 ? 0.0 : (presentCount * 100.0) / totalSessions;
            percentage = Math.round(percentage * 100.0) / 100.0;

            Subject subject = subjectSessions.get(0).getSubject();

            StudentOverallSubjectAttendanceDTO dto = new StudentOverallSubjectAttendanceDTO();
            dto.setSubjectId(subjectId);
            dto.setSubjectName(subject.getName());
            dto.setPercentage(percentage);

            result.add(dto);
        }

        // Sort by subject name (optional)
        result.sort(Comparator.comparing(StudentOverallSubjectAttendanceDTO::getSubjectName));

        return result;
    }

}