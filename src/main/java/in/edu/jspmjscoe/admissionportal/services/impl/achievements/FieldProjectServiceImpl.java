package in.edu.jspmjscoe.admissionportal.services.impl.achievements;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.fieldproject.FieldProjectMapper;
import in.edu.jspmjscoe.admissionportal.model.achievements.FieldProject;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.FieldProjectRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.FieldProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FieldProjectServiceImpl implements FieldProjectService {

    private final FieldProjectRepository fieldProjectRepository;
    private final StudentAcademicYearRepository studentAcademicYearRepository;
    private final FieldProjectMapper fieldProjectMapper;

    @Override
    @Transactional
    public FieldProjectDTO saveFieldProject(FieldProjectDTO fieldProjectDTO, StudentAcademicYear academicYear) {
        FieldProject fieldProject = fieldProjectMapper.toEntity(fieldProjectDTO);
        fieldProject.setStudentAcademicYear(academicYear);

        FieldProject saved = fieldProjectRepository.save(fieldProject);
        return fieldProjectMapper.toDTO(saved);
    }


    @Override
    public FieldProjectDTO getFieldProjectById(Long fieldProjectId) {
        FieldProject fieldProject = fieldProjectRepository.findById(fieldProjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FieldProject not found with ID " + fieldProjectId));
        return fieldProjectMapper.toDTO(fieldProject);
    }
}
