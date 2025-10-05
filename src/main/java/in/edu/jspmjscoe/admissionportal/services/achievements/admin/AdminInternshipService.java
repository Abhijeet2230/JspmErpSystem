package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipMarksUpdateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipUpdateResultDTO;

import java.util.List;

public interface AdminInternshipService {
    InternshipUpdateResultDTO assignInternshipMarksBulk(List<InternshipMarksUpdateDTO> updates);
}
