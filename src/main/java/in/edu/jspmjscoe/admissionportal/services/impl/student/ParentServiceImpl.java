package in.edu.jspmjscoe.admissionportal.services.impl.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.ParentDTO;
import in.edu.jspmjscoe.admissionportal.mappers.student.ParentMapper;
import in.edu.jspmjscoe.admissionportal.model.student.Parent;
import in.edu.jspmjscoe.admissionportal.repositories.student.ParentRepository;
import in.edu.jspmjscoe.admissionportal.services.student.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;

    @Override
    public ParentDTO createParent(ParentDTO parentDTO) {
        Parent parent = parentMapper.toEntity(parentDTO);
        return parentMapper.toDTO(parentRepository.save(parent));
    }

    @Override
    public ParentDTO getParentById(Long id) {
        return parentRepository.findById(id)
                .map(parentMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<ParentDTO> getAllParents() {
        return parentRepository.findAll()
                .stream()
                .map(parentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParentDTO updateParent(Long id, ParentDTO parentDTO) {
        return parentRepository.findById(id).map(parent -> {
            parentMapper.updateParentFromDTO(parentDTO, parent);
            return parentMapper.toDTO(parentRepository.save(parent));
        }).orElse(null);
    }

    @Override
    public void deleteParent(Long id) {
        parentRepository.deleteById(id);
    }
}
