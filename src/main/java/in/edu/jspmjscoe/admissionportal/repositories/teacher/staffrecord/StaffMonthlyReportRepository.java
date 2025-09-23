package in.edu.jspmjscoe.admissionportal.repositories.teacher.staffrecord;

import in.edu.jspmjscoe.admissionportal.model.teacher.staffrecord.StaffMonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffMonthlyReportRepository extends JpaRepository<StaffMonthlyReport, Long> {

    // find all reports of a teacher
    List<StaffMonthlyReport> findByTeacherTeacherId(Long teacherId);

    // find reports for a department
    List<StaffMonthlyReport> findByDepartmentDepartmentId(Long departmentId);

    Optional<StaffMonthlyReport> findByTeacherTeacherIdAndYearAndMonth(Long teacherId, Integer year, Integer month);
}
