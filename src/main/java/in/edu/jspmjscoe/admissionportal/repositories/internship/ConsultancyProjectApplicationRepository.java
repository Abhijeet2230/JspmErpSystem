package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import in.edu.jspmjscoe.admissionportal.model.internship.ConsultancyProjectApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultancyProjectApplicationRepository extends JpaRepository<ConsultancyProjectApplication, Long> {

    List<ConsultancyProjectApplication> findByStudentStudentId(Long studentId);

    List<ConsultancyProjectApplication> findByProjectId(Long projectId);

    List<ConsultancyProjectApplication> findByStatus(ApplicationStatus status);

    boolean existsByStudentStudentIdAndProjectId(Long studentId, Long projectId);
}


