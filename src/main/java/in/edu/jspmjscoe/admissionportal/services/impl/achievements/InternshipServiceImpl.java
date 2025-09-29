package in.edu.jspmjscoe.admissionportal.services.impl.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.internship.InternshipMapper;
import in.edu.jspmjscoe.admissionportal.model.achievements.Internship;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.InternshipRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.InternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternshipServiceImpl implements InternshipService {

    private final InternshipRepository internshipRepository;
    private final StudentAcademicYearRepository studentAcademicYearRepository;
    private final InternshipMapper internshipMapper;

    @Override
    @Transactional
    public InternshipDTO saveInternship(InternshipDTO internshipDTO, StudentAcademicYear academicYear) {
        Internship internship = internshipMapper.toEntity(internshipDTO);
        internship.setStudentAcademicYear(academicYear);
        Internship saved = internshipRepository.save(internship);
        return internshipMapper.toDTO(saved);
    }


    @Override
    public InternshipDTO getInternshipById(Long internshipId) {
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Internship not found with ID " + internshipId));
        return internshipMapper.toDTO(internship);
    }
}
