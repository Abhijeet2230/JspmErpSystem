package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.SSCDTO;
import java.util.List;

public interface SSCService {

    SSCDTO createSSC(SSCDTO sscDTO);

    SSCDTO getSSCById(Long id);

    List<SSCDTO> getAllSSC();

//    SSCDTO updateSSC(Long id, SSCDTO sscDTO);

    void deleteSSC(Long id);
}
