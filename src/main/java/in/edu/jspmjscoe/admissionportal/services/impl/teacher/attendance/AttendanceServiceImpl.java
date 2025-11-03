package in.edu.jspmjscoe.admissionportal.services.impl.teacher.attendance;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance.AdminStudentSubjectAttendanceDTO;
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
import java.util.*;

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

        // ✅ Check for duplicate (same teacher, subject, date, division, and time)
        boolean exists = attendanceSessionRepository.existsDuplicateSession(
                teacher, subject, dto.getDate(), dto.getDivision(), dto.getStartTime(), dto.getEndTime());

        if (exists) {
            throw new RuntimeException("Attendance session already exists for this date and time slot.");
        }

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
    public List<StudentMonthlyAttendanceDTO> getSubjectAttendance(String subjectName, String division, int year) {

        Teacher teacher = currentUserService.getCurrentTeacher();

        Subject subject = subjectRepository.findByNameIgnoreCase(subjectName)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // ✅ Get all sessions for that subject, division, and year (no month filter)
        List<AttendanceSession> sessions = attendanceSessionRepository.findAll().stream()
                .filter(s -> s.getSubject().getName().equalsIgnoreCase(subjectName)
                        && s.getDivision().equalsIgnoreCase(division)
                        && s.getDate().getYear() == year)
                .toList();

        if (sessions.isEmpty()) {
            throw new RuntimeException("No attendance found for given filters");
        }

        // ✅ Get all students from first session
        List<Student> allStudents = sessions.get(0).getStudentAttendances().stream()
                .map(AttendanceStudent::getStudent)
                .toList();

        return allStudents.stream().map(student -> {

            // ✅ Map of date -> number of times present on that day
            Map<String, Integer> attendanceMap = new LinkedHashMap<>();

            sessions.forEach(s -> {
                s.getStudentAttendances().stream()
                        .filter(sa -> sa.getStudent().getStudentId().equals(student.getStudentId()))
                        .findFirst()
                        .ifPresent(sa -> {
                            String dateKey = s.getDate().toString();
                            int currentCount = attendanceMap.getOrDefault(dateKey, 0);

                            // If student is present, increment count
                            if (sa.getStatus().equalsIgnoreCase("present")) {
                                attendanceMap.put(dateKey, currentCount + 1);
                            } else {
                                // If absent, ensure date exists with 0 (only if not already present)
                                attendanceMap.putIfAbsent(dateKey, currentCount);
                            }
                        });
            });

            // ✅ Calculate totals
            int totalSessions = sessions.size(); // total number of lectures conducted
            int totalUniqueDates = attendanceMap.size(); // unique days with attendance recorded
            int presentSessions = attendanceMap.values().stream().mapToInt(Integer::intValue).sum();

            // ✅ Count number of days where student was present at least once
            int presentDays = (int) attendanceMap.values().stream()
                    .filter(v -> v > 0)
                    .count();

            // ✅ Attendance percentage (based on sessions)
            double percentage = totalSessions == 0 ? 0.0 :
                    roundToTwoDecimals((presentSessions * 100.0) / totalSessions);

            // ✅ Fetch rollNo from active academic year for that division
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
                    .totalSessions(totalSessions)
                    .presentSessions(presentSessions)
                    .totalDays(totalUniqueDates)
                    .presentDays(presentDays)
                    .percentage(percentage)
                    .build();

        }).toList();
    }



    @Override
    public List<AdminStudentSubjectAttendanceDTO> getSubjectWiseAttendanceForAdmin(String division, int year) {

        // 1️⃣ Get all sessions for given division and year (no month filter now)
        List<AttendanceSession> sessions = attendanceSessionRepository.findAll().stream()
                .filter(s -> s.getDivision().equalsIgnoreCase(division)
                        && s.getDate().getYear() == year)
                .toList();

        if (sessions.isEmpty()) {
            // return empty list rather than throwing; admin UI can show "no data"
            return Collections.emptyList();
        }

        // 2️⃣ Collect all subjects taught in that division during the year
        LinkedHashSet<String> subjectNames = new LinkedHashSet<>();
        sessions.forEach(s -> subjectNames.add(s.getSubject().getName()));

        // 3️⃣ Collect all students who appear in sessions
        Map<Long, Student> studentMap = new LinkedHashMap<>();
        sessions.forEach(s -> s.getStudentAttendances().forEach(sa ->
                studentMap.putIfAbsent(sa.getStudent().getStudentId(), sa.getStudent()))
        );

        // 4️⃣ Group sessions by subject
        Map<String, List<AttendanceSession>> sessionsBySubject = new HashMap<>();
        for (AttendanceSession s : sessions) {
            String subjectName = s.getSubject().getName();
            sessionsBySubject.computeIfAbsent(subjectName, k -> new ArrayList<>()).add(s);
        }

        // 5️⃣ Compute attendance per student per subject
        List<AdminStudentSubjectAttendanceDTO> result = new ArrayList<>();

        for (Student student : studentMap.values()) {

            AdminStudentSubjectAttendanceDTO dto = new AdminStudentSubjectAttendanceDTO();
            dto.setStudentId(student.getStudentId());
            dto.setStudentName(student.getCandidateName());
            dto.setDivision(division);

            // fetch roll number
            String rollNo = student.getStudentAcademicYears().stream()
                    .filter(ay -> ay.getIsActive() && ay.getDivision().equalsIgnoreCase(division))
                    .findFirst()
                    .map(ay -> String.valueOf(ay.getRollNo()))
                    .orElse(null);
            dto.setRollNo(rollNo);

            double totalPercentageSum = 0.0;
            int subjectCount = 0;
            Map<String, Double> subjectPercentages = new LinkedHashMap<>();

            for (String subjectName : subjectNames) {
                List<AttendanceSession> subjectSessions =
                        sessionsBySubject.getOrDefault(subjectName, Collections.emptyList());

                int totalSessions = subjectSessions.size();
                int presentSessions = (int) subjectSessions.stream()
                        .map(session -> session.getStudentAttendances().stream()
                                .filter(sa -> sa.getStudent().getStudentId().equals(student.getStudentId()))
                                .findFirst()
                                .map(sa -> sa.getStatus().equalsIgnoreCase("present") ? 1 : 0)
                                .orElse(0))
                        .reduce(0, Integer::sum);

                double percentage = totalSessions == 0 ? 0.0 :
                        roundToTwoDecimals((presentSessions * 100.0) / totalSessions);

                subjectPercentages.put(subjectName, percentage);
                totalPercentageSum += percentage;
                subjectCount++;
            }

            double average = subjectCount == 0 ? 0.0 :
                    roundToTwoDecimals(totalPercentageSum / subjectCount);
            dto.setSubjectPercentages(subjectPercentages);
            dto.setAverage(average);

            result.add(dto);
        }

        // 6️⃣ Sort students by rollNo or name
        result.sort((a, b) -> {
            if (a.getRollNo() == null || b.getRollNo() == null)
                return a.getStudentName().compareToIgnoreCase(b.getStudentName());
            try {
                return Integer.valueOf(a.getRollNo()).compareTo(Integer.valueOf(b.getRollNo()));
            } catch (NumberFormatException e) {
                return a.getRollNo().compareToIgnoreCase(b.getRollNo());
            }
        });

        return result;
    }


    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


}
