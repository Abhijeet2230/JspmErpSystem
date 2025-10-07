package in.edu.jspmjscoe.admissionportal.services.impl.minio;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.miniproject.MiniProjectMapper;
import in.edu.jspmjscoe.admissionportal.model.achievements.MiniProject;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import in.edu.jspmjscoe.admissionportal.repositories.achievements.MiniProjectRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentAcademicYearRepository;
import in.edu.jspmjscoe.admissionportal.services.achievements.MiniProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MiniProjectServiceImpl implements MiniProjectService {

    private final MiniProjectRepository miniProjectRepository;
    private final StudentAcademicYearRepository studentAcademicYearRepository;
    private final MiniProjectMapper miniProjectMapper;

    @Override
    @Transactional
    public MiniProjectDTO saveMiniProject(MiniProjectDTO miniProjectDTO, StudentAcademicYear academicYear) {
        MiniProject miniProject = miniProjectMapper.toEntity(miniProjectDTO);
        miniProject.setStudentAcademicYear(academicYear);

        MiniProject saved = miniProjectRepository.save(miniProject);
        return miniProjectMapper.toDTO(saved);
    }


    @Override
    public MiniProjectDTO getMiniProjectById(Long miniProjectId) {
        MiniProject miniProject = miniProjectRepository.findById(miniProjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MiniProject not found with ID " + miniProjectId));
        return miniProjectMapper.toDTO(miniProject);
    }
}
