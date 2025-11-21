package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.GuestLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GuestLectureRepository extends JpaRepository<GuestLecture, Long> {

    List<GuestLecture> findByLectureTitleContainingIgnoreCase(String lectureTitle);

    List<GuestLecture> findBySpeakerNameContainingIgnoreCase(String speakerName);

    List<GuestLecture> findByOrganizationContainingIgnoreCase(String organization);

    List<GuestLecture> findByLectureDate(LocalDate lectureDate);
    
    List<GuestLecture> findByReferredBy_UserId(Long userId);
}


