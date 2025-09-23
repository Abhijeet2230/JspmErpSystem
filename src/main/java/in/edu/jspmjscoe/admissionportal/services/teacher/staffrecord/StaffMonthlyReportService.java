package in.edu.jspmjscoe.admissionportal.services.teacher.staffrecord;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.StaffMonthlyReportDTO;
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
    public void bulkUpdate(List<StaffMonthlyReport> reports) {
        for (StaffMonthlyReport report : reports) {
            if (report.getTeacher() != null && report.getTeacher().getTeacherId() != null) {
                Teacher teacher = teacherRepository.findById(report.getTeacher().getTeacherId())
                        .orElseThrow(() -> new RuntimeException(
                                "Teacher not found with ID " + report.getTeacher().getTeacherId()));
                report.setTeacher(teacher);
                report.setDepartment(teacher.getDepartment());

                // 🔑 Check if record exists for same teacher + year + month
                StaffMonthlyReport existing = staffMonthlyReportRepository
                        .findByTeacherTeacherIdAndYearAndMonth(
                                teacher.getTeacherId(),
                                report.getYear(),
                                report.getMonth()
                        )
                        .orElse(null);

                if (existing != null) {
                    // Update existing
                    existing.setDaysInMonth(report.getDaysInMonth());
                    existing.setFirstAndThirdSaturday(report.getFirstAndThirdSaturday());
                    existing.setSundays(report.getSundays());
                    existing.setOtherHolidays(report.getOtherHolidays());
                    existing.setActualWorkingDays(report.getActualWorkingDays());
                    existing.setWorkingOnHoliday(report.getWorkingOnHoliday());
                    existing.setCl(report.getCl());
                    existing.setCompOff(report.getCompOff());
                    existing.setMl(report.getMl());
                    existing.setEl(report.getEl());
                    existing.setOdOrDl(report.getOdOrDl());
                    existing.setLwp(report.getLwp());
                    existing.setSpecialLeave(report.getSpecialLeave());
                    existing.setGatePass(report.getGatePass());
                    existing.setActualPresentDays(report.getActualPresentDays());
                    existing.setPaidDays(report.getPaidDays());
                    existing.setPresentPercentage(report.getPresentPercentage());
                    existing.setRemark(report.getRemark());

                    staffMonthlyReportRepository.save(existing);
                } else {
                    // Insert new
                    staffMonthlyReportRepository.save(report);
                }
            }
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
            StaffMonthlyReportDTO dto = StaffMonthlyReportDTO.builder()
                    .teacher(staffMonthlyReportMapper.mapTeacherToBasicDTO(teacher))
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

            dtos.add(dto);
        }

        return dtos;
    }
}
