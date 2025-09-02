package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.CETDTO;
import java.util.List;

public interface CETService {

    CETDTO createCET(CETDTO cetDTO);

    CETDTO getCETById(Long id);

    List<CETDTO> getAllCET();

//    CETDTO updateCET(Long id, CETDTO cetDTO);

    void deleteCET(Long id);
}
