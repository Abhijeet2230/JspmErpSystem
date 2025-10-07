package in.edu.jspmjscoe.admissionportal.services.impl.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceSessionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentMonthlyAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.attendance.AttendanceMapper;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.attendance.AttendanceSession;
import in.edu.jspmjscoe.admissionportal.model.teacher.attendance.AttendanceStudent;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.CourseRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.DepartmentRepository;
import in.edu.jspmjscoe.admissionportal.repositories.subject.SubjectRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.attendance.AttendanceSessionRepository;
import in.edu.jspmjscoe.admissionportal.security.services.CurrentUserService;
import in.edu.jspmjscoe.admissionportal.services.teacher.attendance.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceSessionRepository attendanceSessionRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final AttendanceMapper attendanceMapper;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final CurrentUserService currentUserService;

    @Override
    public List<StudentAttendanceDTO> getStudentsForAttendance(String departmentName,
                                                               String subjectName,
                                                               String division) {

        Teacher teacher = currentUserService.getCurrentTeacher();

        Department department = departmentRepository.findByNameIgnoreCase(departmentName)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Subject subject = subjectRepository.findByNameIgnoreCase(subjectName)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Course course = subject.getCourse();
        Integer semester = subject.getSemester();

        List<Student> students = studentRepository.findStudentsByCourseSemesterDivision(course, semester, division);

        return students.stream()
                .map(student -> StudentAttendanceDTO.builder()
                        .studentId(student.getStudentId())
                        .candidateName(student.getCandidateName())
                        .courseName(course.getName())
                        .departmentName(department.getName())
                        .semester(semester)
                        .division(division)
                        .rollNo(student.getStudentAcademicYears().stream()
                                .filter(ay -> ay.getSemester().equals(semester)
                                        && ay.getDivision().equals(division)
                                        && ay.getIsActive())
                                .findFirst()
                                .map(StudentAcademicYear::getRollNo)
                                .orElse(null))
                        .build())
                .toList();
    }

    @Override
    public AttendanceSessionDTO createAttendanceSession(AttendanceSessionDTO dto) {
        Teacher teacher = currentUserService.getCurrentTeacher();

        Department department = departmentRepository.findByNameIgnoreCase(dto.getDepartmentName())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Subject subject = subjectRepository.findByNameIgnoreCase(dto.getSubjectName())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        AttendanceSession session = AttendanceSession.builder()
                .teacher(teacher)
                .department(department)
                .subject(subject)
                .division(dto.getDivision())
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();

        if (dto.getStudentAttendances() != null) {
            dto.getStudentAttendances().forEach(s -> {
                Student student = studentRepository.findById(s.getStudentId())
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                AttendanceStudent as = AttendanceStudent.builder()
                        .student(student)
                        .status(s.getStatus())
                        .build();
                session.addAttendanceStudent(as);
            });
        }

        attendanceSessionRepository.save(session);
        return attendanceMapper.toSessionDto(session);
    }

    @Override
    public List<AttendanceSessionDTO> getAttendanceSessionsByFilter(String subjectName, String division, LocalDate date) {
        List<AttendanceSession> sessions = attendanceSessionRepository
                .findBySubject_NameAndDivisionAndDate(subjectName, division, date);

        return sessions.stream()
                .map(attendanceMapper::toSessionDto)
                .toList();
    }

    @Override
    public List<StudentMonthlyAttendanceDTO> getMonthlyAttendance(String subjectName, String division, int year, int month) {

        Teacher teacher = currentUserService.getCurrentTeacher();

        Subject subject = subjectRepository.findByNameIgnoreCase(subjectName)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Get all sessions for that subject, division, and month
        List<AttendanceSession> sessions = attendanceSessionRepository.findAll().stream()
                .filter(s -> s.getSubject().getName().equalsIgnoreCase(subjectName)
                        && s.getDivision().equalsIgnoreCase(division)
                        && s.getDate().getYear() == year
                        && s.getDate().getMonthValue() == month)
                .toList();

        if (sessions.isEmpty()) {
            throw new RuntimeException("No attendance found for given filters");
        }

        // Get all students involved in those sessions
        List<Student> allStudents = sessions.get(0).getStudentAttendances().stream()
                .map(AttendanceStudent::getStudent)
                .toList();

        return allStudents.stream().map(student -> {

            // ✅ Avoid duplicate key issue (use LinkedHashMap and merge)
            Map<String, Integer> attendanceMap = new java.util.LinkedHashMap<>();
            sessions.forEach(s -> {
                s.getStudentAttendances().stream()
                        .filter(sa -> sa.getStudent().getStudentId().equals(student.getStudentId()))
                        .findFirst()
                        .ifPresent(sa -> attendanceMap.put(
                                s.getDate().toString(),
                                sa.getStatus().equalsIgnoreCase("present") ? 1 : 0
                        ));
            });

            int totalDays = attendanceMap.size();
            int presentDays = (int) attendanceMap.values().stream().filter(v -> v == 1).count();
            double percentage = totalDays == 0 ? 0.0 : (presentDays * 100.0) / totalDays;

            // ✅ Fetch rollNo from active academic year for that semester/division
            String rollNo = student.getStudentAcademicYears().stream()
                    .filter(ay -> ay.getIsActive() && ay.getDivision().equalsIgnoreCase(division))
                    .findFirst()
                    .map(ay -> String.valueOf(ay.getRollNo()))
                    .orElse(null);

            return StudentMonthlyAttendanceDTO.builder()
                    .studentId(student.getStudentId())
                    .studentName(student.getCandidateName())
                    .rollNo(rollNo)
                    .division(division)
                    .subjectName(subject.getName())
                    .attendanceByDate(attendanceMap)
                    .totalDays(totalDays)
                    .presentDays(presentDays)
                    .percentage(percentage)
                    .build();
        }).toList();
    }
}
