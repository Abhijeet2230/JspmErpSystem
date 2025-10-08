package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.GuestLectureDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GuestLectureService {
    
    GuestLectureDTO createGuestLecture(GuestLectureDTO guestLectureDTO);
    
    GuestLectureDTO updateGuestLecture(Long lectureId, GuestLectureDTO guestLectureDTO);
    
    Optional<GuestLectureDTO> getGuestLectureById(Long lectureId);
    
    List<GuestLectureDTO> getAllGuestLectures();
    
    List<GuestLectureDTO> getGuestLecturesByTitle(String title);
    
    List<GuestLectureDTO> getGuestLecturesBySpeaker(String speakerName);
    
    List<GuestLectureDTO> getGuestLecturesByDate(LocalDate lectureDate);
    
    List<GuestLectureDTO> getGuestLecturesByOrganization(String organization);
    
    void deleteGuestLecture(Long lectureId);
}