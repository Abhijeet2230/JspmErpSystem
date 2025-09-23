package in.edu.jspmjscoe.admissionportal.utils;

import java.time.DayOfWeek;
import java.time.YearMonth;

public class StaffMonthlyReportUtil {

    public static ReportDays computeMonthDays(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();
        int sundays = 0;
        int firstAndThirdSaturdays = 0;

        for (int day = 1; day <= daysInMonth; day++) {
            DayOfWeek dow = ym.atDay(day).getDayOfWeek();
            if (dow == DayOfWeek.SUNDAY) {
                sundays++;
            } else if (dow == DayOfWeek.SATURDAY) {
                // 1st Saturday = 1-7, 3rd Saturday = 15-21
                if ((day >= 1 && day <= 7) || (day >= 15 && day <= 21)) {
                    firstAndThirdSaturdays++;
                }
            }
        }
        return new ReportDays(daysInMonth, sundays, firstAndThirdSaturdays);
    }

    public static class ReportDays {
        public final int daysInMonth;
        public final int sundays;
        public final int firstAndThirdSaturdays;

        public ReportDays(int daysInMonth, int sundays, int firstAndThirdSaturdays) {
            this.daysInMonth = daysInMonth;
            this.sundays = sundays;
            this.firstAndThirdSaturdays = firstAndThirdSaturdays;
        }
    }
}
