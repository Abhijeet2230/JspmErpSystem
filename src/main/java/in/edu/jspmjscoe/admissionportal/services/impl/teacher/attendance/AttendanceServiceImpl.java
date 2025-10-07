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
            double percentage = totalDays == 0 ? 0.0 : roundToTwoDecimals((presentDays * 100.0) / totalDays);

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

    @Override
    public List<AdminStudentSubjectAttendanceDTO> getMonthlySubjectWiseAttendanceForAdmin(String division, int year, int month) {

        // 1. Get all sessions for given division and month/year
        List<AttendanceSession> sessions = attendanceSessionRepository.findAll().stream()
                .filter(s -> s.getDivision().equalsIgnoreCase(division)
                        && s.getDate().getYear() == year
                        && s.getDate().getMonthValue() == month)
                .toList();

        if (sessions.isEmpty()) {
            // return empty list rather than throwing; admin UI can show "no data"
            return java.util.Collections.emptyList();
        }

        // 2. Collect all subject names (preserve insertion order if you like)
        LinkedHashSet<String> subjectNames = new java.util.LinkedHashSet<>();
        sessions.forEach(s -> subjectNames.add(s.getSubject().getName()));

        // 3. Collect all students who appear in sessions (some students may appear only in one subject)
        Map<Long,Student> studentMap = new LinkedHashMap<>();
        sessions.forEach(s -> {
            s.getStudentAttendances().forEach(sa -> {
                studentMap.putIfAbsent(sa.getStudent().getStudentId(), sa.getStudent());
            });
        });

        // 4. Precompute for each subject -> list of sessions (for easier totals)
        Map<String, List<AttendanceSession>> sessionsBySubject = new HashMap<>();
        for (AttendanceSession s : sessions) {
            String subName = s.getSubject().getName();
            sessionsBySubject.computeIfAbsent(subName, k -> new ArrayList<>()).add(s);
        }

        // 5. For each student compute per-subject percentage and average
        List<AdminStudentSubjectAttendanceDTO> result = new ArrayList<>();

        for (Student student : studentMap.values()) {

            AdminStudentSubjectAttendanceDTO dto = new AdminStudentSubjectAttendanceDTO();
            dto.setStudentId(student.getStudentId());
            dto.setStudentName(student.getCandidateName());
            dto.setDivision(division);

            // Get rollNo same way as your other method: active academic year with division
            String rollNo = student.getStudentAcademicYears().stream()
                    .filter(ay -> ay.getIsActive() && ay.getDivision().equalsIgnoreCase(division))
                    .findFirst()
                    .map(ay -> String.valueOf(ay.getRollNo()))
                    .orElse(null);
            dto.setRollNo(rollNo);

            double sumPercentages = 0.0;
            int subjectCount = 0;

            // use LinkedHashMap to preserve ordering of subjects
            Map<String, Double> subjectPercentages = new LinkedHashMap<>();

            for (String subjectName : subjectNames) {
                List<AttendanceSession> sessionsForSubject = sessionsBySubject.getOrDefault(subjectName, Collections.emptyList());

                // totalDays = number of distinct session dates for that subject in month
                // (If multiple sessions same date exist for same subject, you might want to consider unique dates;
                //  here we'll count each session row as one "day" because existing attendance uses a session per class.)
                int totalDays = sessionsForSubject.size();

                // count present days for this student for this subject
                int presentDays = 0;
                for (AttendanceSession session : sessionsForSubject) {
                    session.getStudentAttendances().stream()
                            .filter(sa -> sa.getStudent().getStudentId().equals(student.getStudentId()))
                            .findFirst()
                            .ifPresent(sa -> {
                                if (sa.getStatus() != null && sa.getStatus().equalsIgnoreCase("present")) {
                                    // increment via side-effect closure not ideal — do simple way instead
                                }
                            });
                }
                // above closure won't increment presentDays due to final requirement; do it plainly:
                presentDays = (int) sessionsForSubject.stream().map(session ->
                        session.getStudentAttendances().stream()
                                .filter(sa -> sa.getStudent().getStudentId().equals(student.getStudentId()))
                                .findFirst()
                                .map(sa -> sa.getStatus().equalsIgnoreCase("present") ? 1 : 0)
                                .orElse(0)
                ).reduce(0, Integer::sum);

                double percentage = totalDays == 0 ? 0.0 : roundToTwoDecimals((presentDays * 100.0) / totalDays);

                subjectPercentages.put(subjectName, percentage);

                sumPercentages += percentage;
                subjectCount++;
            }

            double average = subjectCount == 0 ? 0.0 : roundToTwoDecimals(sumPercentages / subjectCount);
            dto.setSubjectPercentages(subjectPercentages);
            dto.setAverage(average);

            result.add(dto);
        }

        // Optional: sort by rollNo (if roll numbers numeric) or by student name
        result.sort((a, b) -> {
            if (a.getRollNo() == null || b.getRollNo() == null) return a.getStudentName().compareToIgnoreCase(b.getStudentName());
            try {
                Integer ra = Integer.valueOf(a.getRollNo());
                Integer rb = Integer.valueOf(b.getRollNo());
                return ra.compareTo(rb);
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
