package in.edu.jspmjscoe.admissionportal.services.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.HSCDTO;
import java.util.List;

public interface HSCService {

    HSCDTO createHSC(HSCDTO hscDTO);

    HSCDTO getHSCById(Long id);

    List<HSCDTO> getAllHSC();

//    HSCDTO updateHSC(Long id, HSCDTO hscDTO);

    void deleteHSC(Long id);
}
