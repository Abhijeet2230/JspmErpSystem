package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipUpdateResultDTO;

import java.util.Map;

public interface AdminInternshipService {
    InternshipUpdateResultDTO assignInternshipMarksBulk(Map<Long, Double> internshipMarks);
}
