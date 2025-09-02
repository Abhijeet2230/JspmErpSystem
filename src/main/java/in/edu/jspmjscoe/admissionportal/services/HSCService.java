package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.HSCDTO;
import java.util.List;

public interface HSCService {

    HSCDTO createHSC(HSCDTO hscDTO);

    HSCDTO getHSCById(Long id);

    List<HSCDTO> getAllHSC();

//    HSCDTO updateHSC(Long id, HSCDTO hscDTO);

    void deleteHSC(Long id);
}
