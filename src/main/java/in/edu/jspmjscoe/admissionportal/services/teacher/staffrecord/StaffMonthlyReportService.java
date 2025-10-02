package in.edu.jspmjscoe.admissionportal.services.teacher.staffrecord;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.BulkStaffUpdateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.StaffMonthlyReportDTO;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.staffrecord.BulkStaffUpdateMapper;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.staffrecord.StaffMonthlyReportMapper;
import in.edu.jspmjscoe.admissionportal.model.teacher.staffrecord.StaffMonthlyReport;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.staffrecord.StaffMonthlyReportRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffMonthlyReportService {

    private final StaffMonthlyReportRepository staffMonthlyReportRepository;
    private final TeacherRepository teacherRepository;
    private final StaffMonthlyReportMapper staffMonthlyReportMapper;
    private final BulkStaffUpdateMapper bulkStaffUpdateMapper;

    public StaffMonthlyReport initializeForTeacher(Long teacherId, int year, int month) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();

        int sundays = 0;
        int firstAndThirdSaturday = 0;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = ym.atDay(day);
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) sundays++;
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                int weekOfMonth = (day + 6) / 7;
                if (weekOfMonth == 1 || weekOfMonth == 3) firstAndThirdSaturday++;
            }
        }

        StaffMonthlyReport report = StaffMonthlyReport.builder()
                .teacher(teacher)
                .department(teacher.getDepartment())
                .year(year)
                .month(month)
                .daysInMonth(daysInMonth)
                .firstAndThirdSaturday(firstAndThirdSaturday)
                .sundays(sundays)
                .otherHolidays(0)
                .actualWorkingDays(0)
                .workingOnHoliday(0)
                .cl(0)
                .compOff(0)
                .ml(0)
                .el(0)
                .odOrDl(0)
                .lwp(0)
                .specialLeave(0)
                .gatePass(0)
                .actualPresentDays(0)
                .paidDays(0)
                .presentPercentage(0.0)
                .remark("")
                .build();

        return staffMonthlyReportRepository.save(report);
    }

    @Transactional
    public void bulkUpdate(List<BulkStaffUpdateDTO> dtos) {
        for (BulkStaffUpdateDTO dto : dtos) {
            Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found with ID " + dto.getTeacherId()));

            StaffMonthlyReport existing = staffMonthlyReportRepository
                    .findByTeacherTeacherIdAndYearAndMonth(dto.getTeacherId(), dto.getYear(), dto.getMonth())
                    .orElseThrow(() -> new RuntimeException(
                            "Report not found for teacher " + dto.getTeacherId() + " in " + dto.getMonth() + "/" + dto.getYear()
                    ));

            // Apply only changing fields
            bulkStaffUpdateMapper.updateEntityFromDto(dto, existing);

            staffMonthlyReportRepository.save(existing);
        }
    }


    public List<StaffMonthlyReport> getReportsByTeacher(Long teacherId) {
        return staffMonthlyReportRepository.findByTeacherTeacherId(teacherId);
    }

    public List<StaffMonthlyReport> getReportsByDepartment(Long departmentId) {
        return staffMonthlyReportRepository.findByDepartmentDepartmentId(departmentId);
    }

    // ----------------- NEW: Generate DTO list for frontend -----------------
    public List<StaffMonthlyReportDTO> generateMonthlyReports(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();

        int sundays = 0;
        int firstAndThirdSaturday = 0;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = ym.atDay(day);
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) sundays++;
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                int weekOfMonth = (day + 6) / 7;
                if (weekOfMonth == 1 || weekOfMonth == 3) firstAndThirdSaturday++;
            }
        }

        List<Teacher> teachers = teacherRepository.findAll();
        List<StaffMonthlyReportDTO> dtos = new ArrayList<>();

        for (Teacher teacher : teachers) {
            // Check if report exists
            StaffMonthlyReport report = staffMonthlyReportRepository
                    .findByTeacherTeacherIdAndYearAndMonth(teacher.getTeacherId(), year, month)
                    .orElseGet(() -> initializeForTeacher(teacher.getTeacherId(), year, month)); // Auto-initialize if missing

            // Update calculated fields in case month details changed
            report.setDaysInMonth(daysInMonth);
            report.setFirstAndThirdSaturday(firstAndThirdSaturday);
            report.setSundays(sundays);
            staffMonthlyReportRepository.save(report); // ensure DB is updated

            dtos.add(staffMonthlyReportMapper.toDto(report));
        }

        return dtos;
    }

}
