package in.edu.jspmjscoe.admissionportal.services.impl.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.GuestLectureDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.internship.GuestLectureMapper;
import in.edu.jspmjscoe.admissionportal.model.internship.GuestLecture;
import in.edu.jspmjscoe.admissionportal.repositories.internship.GuestLectureRepository;
import in.edu.jspmjscoe.admissionportal.services.internship.GuestLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestLectureServiceImpl implements GuestLectureService {

    private final GuestLectureRepository guestLectureRepository;
    private final GuestLectureMapper guestLectureMapper;

    @Override
    public GuestLectureDTO createGuestLecture(GuestLectureDTO guestLectureDTO) {
        GuestLecture guestLecture = guestLectureMapper.toEntity(guestLectureDTO);
        GuestLecture savedGuestLecture = guestLectureRepository.save(guestLecture);
        return guestLectureMapper.toDTO(savedGuestLecture);
    }

    @Override
    public GuestLectureDTO updateGuestLecture(Long lectureId, GuestLectureDTO guestLectureDTO) {
        GuestLecture existingGuestLecture = guestLectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest lecture not found with id: " + lectureId));
        
        // Use mapper to update entity from DTO
        guestLectureMapper.updateEntityFromDto(guestLectureDTO, existingGuestLecture);
        
        GuestLecture updatedGuestLecture = guestLectureRepository.save(existingGuestLecture);
        return guestLectureMapper.toDTO(updatedGuestLecture);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuestLectureDTO> getGuestLectureById(Long lectureId) {
        return guestLectureRepository.findById(lectureId)
                .map(guestLectureMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestLectureDTO> getAllGuestLectures() {
        return guestLectureRepository.findAll()
                .stream()
                .map(guestLectureMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestLectureDTO> getGuestLecturesByTitle(String title) {
        return guestLectureRepository.findByLectureTitleContainingIgnoreCase(title)
                .stream()
                .map(guestLectureMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestLectureDTO> getGuestLecturesBySpeaker(String speakerName) {
        return guestLectureRepository.findBySpeakerNameContainingIgnoreCase(speakerName)
                .stream()
                .map(guestLectureMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestLectureDTO> getGuestLecturesByDate(LocalDate lectureDate) {
        return guestLectureRepository.findByLectureDate(lectureDate)
                .stream()
                .map(guestLectureMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestLectureDTO> getGuestLecturesByOrganization(String organization) {
        return guestLectureRepository.findByOrganizationContainingIgnoreCase(organization)
                .stream()
                .map(guestLectureMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteGuestLecture(Long lectureId) {
        if (!guestLectureRepository.existsById(lectureId)) {
            throw new ResourceNotFoundException("Guest lecture not found with id: " + lectureId);
        }
        guestLectureRepository.deleteById(lectureId);
    }
}