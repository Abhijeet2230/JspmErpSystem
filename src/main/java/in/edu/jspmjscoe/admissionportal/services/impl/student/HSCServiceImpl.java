package in.edu.jspmjscoe.admissionportal.services.impl.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.HSCDTO;
import in.edu.jspmjscoe.admissionportal.mappers.student.HSCMapper;
import in.edu.jspmjscoe.admissionportal.model.student.HSC;
import in.edu.jspmjscoe.admissionportal.repositories.student.HSCRepository;
import in.edu.jspmjscoe.admissionportal.services.student.HSCService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HSCServiceImpl implements HSCService {

    private final HSCRepository hscRepository;
    private final HSCMapper hscMapper;

    @Override
    public HSCDTO createHSC(HSCDTO hscDTO) {
        HSC hsc = hscMapper.toEntity(hscDTO);
        return hscMapper.toDTO(hscRepository.save(hsc));
    }

    @Override
    public HSCDTO getHSCById(Long id) {
        return hscRepository.findById(id).map(hscMapper::toDTO).orElse(null);
    }

    @Override
    public List<HSCDTO> getAllHSC() {
        return hscRepository.findAll().stream().map(hscMapper::toDTO).collect(Collectors.toList());
    }

//    @Override
//    public HSCDTO updateHSC(Long id, HSCDTO hscDTO) {
//        return hscRepository.findById(id).map(hsc -> {
//            hscMapper.updateHSCFromDTO(hscDTO, hsc);
//            return hscMapper.toDTO(hscRepository.save(hsc));
//        }).orElse(null);
//    }

    @Override
    public void deleteHSC(Long id) {
        hscRepository.deleteById(id);
    }
}
