package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.ConsultancyProjectWork;
import in.edu.jspmjscoe.admissionportal.model.internship.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultancyProjectWorkRepository extends JpaRepository<ConsultancyProjectWork, Long> {

    List<ConsultancyProjectWork> findByOrganizationContainingIgnoreCase(String organization);

    List<ConsultancyProjectWork> findByStatus(ProjectStatus status);

    List<ConsultancyProjectWork> findByProjectTitleContainingIgnoreCase(String projectTitle);
}


