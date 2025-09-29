package in.edu.jspmjscoe.admissionportal.repositories.teacher.appriasal;

import in.edu.jspmjscoe.admissionportal.model.teacher.appriasal.TeacherAppraisal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherAppraisalRepository extends JpaRepository<TeacherAppraisal, Long> {

    // Find all appraisals for a given teacherId
    List<TeacherAppraisal> findByTeacher_TeacherId(Long teacherId);

    // Optional: Filter by academic year
    List<TeacherAppraisal> findByTeacher_TeacherIdAndAcademicYear(Long teacherId, String academicYear);

    // Optional: Filter by department
    List<TeacherAppraisal> findByTeacher_Department_Name(String departmentName);
}
