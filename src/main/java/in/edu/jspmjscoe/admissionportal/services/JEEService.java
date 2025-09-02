package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.JEEDTO;
import java.util.List;

public interface JEEService {

    JEEDTO createJEE(JEEDTO jeeDTO);

    JEEDTO getJEEById(Long id);

    List<JEEDTO> getAllJEE();

    JEEDTO updateJEE(Long id, JEEDTO jeeDTO);

    void deleteJEE(Long id);
}
