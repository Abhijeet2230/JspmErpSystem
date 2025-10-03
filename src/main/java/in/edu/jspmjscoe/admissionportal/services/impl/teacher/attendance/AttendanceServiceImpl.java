package in.edu.jspmjscoe.admissionportal.services.impl.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.student.StudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceSessionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentAttendanceDTO;
import in.edu.jspmjscoe.admissionportal.mappers.student.StudentMapper;
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
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.attendance.AttendanceSessionRepository;
import in.edu.jspmjscoe.admissionportal.security.services.CurrentUserService;
import in.edu.jspmjscoe.admissionportal.services.teacher.attendance.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceSessionRepository attendanceSessionRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final AttendanceMapper attendanceMapper;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final CurrentUserService currentUserService;
    private final StudentMapper studentMapper;

    @Override
    public List<StudentAttendanceDTO> getStudentsForAttendance(String departmentName,
                                                               String courseName,
                                                               Integer semester,
                                                               String subjectName,
                                                               String division) {

        // Resolve department
        Department department = departmentRepository.findByNameIgnoreCase(departmentName)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Resolve course
        Course course = courseRepository.findByNameAndDepartment(courseName, department)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Resolve subject (optional validation)
        Subject subject = subjectRepository.findByNameAndCourseAndSemester(subjectName, course, semester)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Fetch students via StudentAcademicYear
        List<Student> students = studentRepository.findStudentsByCourseSemesterDivision(course, semester, division);

        // Map to StudentAttendanceDTO
        return students.stream()
                .map(student -> StudentAttendanceDTO.builder()
                        .studentId(student.getStudentId())
                        .candidateName(student.getCandidateName())
                        .courseName(course.getName())
                        .departmentName(department.getName())
                        .semester(semester)
                        .division(division)
                        .rollNo(student.getStudentAcademicYears().stream()
                                .filter(ay -> ay.getSemester().equals(semester) && ay.getDivision().equals(division) && ay.getIsActive())
                                .findFirst()
                                .map(StudentAcademicYear::getRollNo)
                                .orElse(null))
                        .build())
                .toList();
    }


    @Override
    public AttendanceSessionDTO createAttendanceSession(AttendanceSessionDTO dto) {

        // Get current teacher from SecurityContext
        Teacher teacher = currentUserService.getCurrentTeacher(); // implement based on your auth

        // Resolve department, course, subject from names
        Department department = departmentRepository.findByNameIgnoreCase(dto.getDepartmentName())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Course course = courseRepository.findByNameAndDepartment(dto.getCourseName(), department)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Subject subject = subjectRepository.findByNameAndCourseAndSemester(
                        dto.getSubjectName(), course, dto.getSemester())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        AttendanceSession session = AttendanceSession.builder()
                .teacher(teacher)
                .department(department)
                .course(course)
                .semester(dto.getSemester())
                .subject(subject)
                .division(dto.getDivision())
                .date(dto.getDate())
                .periodNumber(dto.getPeriodNumber())
                .build();

        dto.getStudentAttendances().forEach(s -> {
            Student student = studentRepository.findById(s.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            AttendanceStudent as = AttendanceStudent.builder()
                    .student(student)
                    .status(s.getStatus())
                    .build();
            session.addAttendanceStudent(as);
        });

        attendanceSessionRepository.save(session);
        return attendanceMapper.toSessionDto(session);
    }



    @Override
    public AttendanceSessionDTO getAttendanceSession(Long sessionId) {
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return attendanceMapper.toSessionDto(session);
    }
}
