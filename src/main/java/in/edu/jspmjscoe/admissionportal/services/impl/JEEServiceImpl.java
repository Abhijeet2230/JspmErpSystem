package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.JEEDTO;
import in.edu.jspmjscoe.admissionportal.mappers.JEEMapper;
import in.edu.jspmjscoe.admissionportal.model.JEE;
import in.edu.jspmjscoe.admissionportal.repositories.JEERepository;
import in.edu.jspmjscoe.admissionportal.services.JEEService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JEEServiceImpl implements JEEService {

    private final JEERepository jeeRepository;
    private final JEEMapper jeeMapper;

    @Override
    public JEEDTO createJEE(JEEDTO jeeDTO) {
        JEE jee = jeeMapper.toEntity(jeeDTO);
        return jeeMapper.toDTO(jeeRepository.save(jee));
    }

    @Override
    public JEEDTO getJEEById(Long id) {
        return jeeRepository.findById(id).map(jeeMapper::toDTO).orElse(null);
    }

    @Override
    public List<JEEDTO> getAllJEE() {
        return jeeRepository.findAll().stream().map(jeeMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public JEEDTO updateJEE(Long id, JEEDTO jeeDTO) {
        return jeeRepository.findById(id).map(jee -> {
            jeeMapper.updateJEEFromDTO(jeeDTO, jee);
            return jeeMapper.toDTO(jeeRepository.save(jee));
        }).orElse(null);
    }

    @Override
    public void deleteJEE(Long id) {
        jeeRepository.deleteById(id);
    }
}
