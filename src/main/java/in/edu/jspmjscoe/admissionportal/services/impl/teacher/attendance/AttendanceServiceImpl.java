package in.edu.jspmjscoe.admissionportal.services.impl.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AttendanceSessionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.StudentAttendanceDTO;
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

}
