package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.ApplicationStatus;
import in.edu.jspmjscoe.admissionportal.model.internship.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {

    List<InternshipApplication> findByStudentStudentId(Long studentId);

    List<InternshipApplication> findByStudentProfileProfileId(Long profileId);

    List<InternshipApplication> findByInternshipInternshipId(Long internshipId);

    List<InternshipApplication> findByStatus(ApplicationStatus status);

    boolean existsByStudentStudentIdAndInternshipInternshipId(Long studentId, Long internshipId);

    boolean existsByStudentProfileProfileIdAndInternshipInternshipId(Long profileId, Long internshipId);
}


