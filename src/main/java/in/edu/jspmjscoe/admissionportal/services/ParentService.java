package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.ParentDTO;
import java.util.List;

public interface ParentService {

    ParentDTO createParent(ParentDTO parentDTO);

    ParentDTO getParentById(Long id);

    List<ParentDTO> getAllParents();

    ParentDTO updateParent(Long id, ParentDTO parentDTO);

    void deleteParent(Long id);
}
