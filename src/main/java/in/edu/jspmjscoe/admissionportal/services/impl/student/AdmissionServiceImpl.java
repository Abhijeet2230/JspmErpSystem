package in.edu.jspmjscoe.admissionportal.services.impl.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.AdmissionDTO;
import in.edu.jspmjscoe.admissionportal.mappers.student.AdmissionMapper;
import in.edu.jspmjscoe.admissionportal.model.student.Admission;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.repositories.student.AdmissionRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.student.AdmissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final StudentRepository studentRepository;
    private final AdmissionMapper admissionMapper;

    @Override
    public AdmissionDTO createAdmission(AdmissionDTO admissionDTO, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Admission admission = admissionMapper.toEntity(admissionDTO);
        admission.setStudent(student);

        return admissionMapper.toDTO(admissionRepository.save(admission));
    }

    @Override
    public AdmissionDTO getAdmissionById(Long id) {
        return admissionRepository.findById(id)
                .map(admissionMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Admission not found"));
    }

    @Override
    public List<AdmissionDTO> getAllAdmissions() {
        return admissionRepository.findAll()
                .stream()
                .map(admissionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdmissionDTO updateAdmission(Long id, AdmissionDTO admissionDTO) {
        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admission not found"));

        admissionMapper.updateAdmissionFromDTO(admissionDTO, admission);

        return admissionMapper.toDTO(admissionRepository.save(admission));
    }

    @Override
    public void deleteAdmission(Long id) {
        if (!admissionRepository.existsById(id)) {
            throw new EntityNotFoundException("Admission not found");
        }
        admissionRepository.deleteById(id);
    }
}
