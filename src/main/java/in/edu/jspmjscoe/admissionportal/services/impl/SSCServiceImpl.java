package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.SSCDTO;
import in.edu.jspmjscoe.admissionportal.mappers.SSCMapper;
import in.edu.jspmjscoe.admissionportal.model.SSC;
import in.edu.jspmjscoe.admissionportal.repositories.SSCRepository;
import in.edu.jspmjscoe.admissionportal.services.SSCService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SSCServiceImpl implements SSCService {

    private final SSCRepository sscRepository;
    private final SSCMapper sscMapper;

    @Override
    public SSCDTO createSSC(SSCDTO sscDTO) {
        SSC ssc = sscMapper.toEntity(sscDTO);
        return sscMapper.toDTO(sscRepository.save(ssc));
    }

    @Override
    public SSCDTO getSSCById(Long id) {
        return sscRepository.findById(id).map(sscMapper::toDTO).orElse(null);
    }

    @Override
    public List<SSCDTO> getAllSSC() {
        return sscRepository.findAll().stream().map(sscMapper::toDTO).collect(Collectors.toList());
    }

//    @Override
//    public SSCDTO updateSSC(Long id, SSCDTO sscDTO) {
//        return sscRepository.findById(id).map(ssc -> {
//            sscMapper.updateSSCFromDTO(sscDTO, ssc);
//            return sscMapper.toDTO(sscRepository.save(ssc));
//        }).orElse(null);
//    }

    @Override
    public void deleteSSC(Long id) {
        sscRepository.deleteById(id);
    }
}
