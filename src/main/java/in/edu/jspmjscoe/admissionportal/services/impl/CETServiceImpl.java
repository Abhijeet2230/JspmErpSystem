package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.CETDTO;
import in.edu.jspmjscoe.admissionportal.mappers.CETMapper;
import in.edu.jspmjscoe.admissionportal.model.CET;
import in.edu.jspmjscoe.admissionportal.repositories.CETRepository;
import in.edu.jspmjscoe.admissionportal.services.CETService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CETServiceImpl implements CETService {

    private final CETRepository cetRepository;
    private final CETMapper cetMapper;

    @Override
    public CETDTO createCET(CETDTO cetDTO) {
        CET cet = cetMapper.toEntity(cetDTO);
        return cetMapper.toDTO(cetRepository.save(cet));
    }

    @Override
    public CETDTO getCETById(Long id) {
        return cetRepository.findById(id).map(cetMapper::toDTO).orElse(null);
    }

    @Override
    public List<CETDTO> getAllCET() {
        return cetRepository.findAll().stream().map(cetMapper::toDTO).collect(Collectors.toList());
    }

//    @Override
//    public CETDTO updateCET(Long id, CETDTO cetDTO) {
//        return cetRepository.findById(id).map(cet -> {
//            cetMapper.updateCETFromDTO(cetDTO, cet);
//            return cetMapper.toDTO(cetRepository.save(cet));
//        }).orElse(null);
//    }

    @Override
    public void deleteCET(Long id) {
        cetRepository.deleteById(id);
    }
}
