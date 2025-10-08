package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.TrainingSkillWorkshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSkillWorkshopRepository extends JpaRepository<TrainingSkillWorkshop, Long> {

    List<TrainingSkillWorkshop> findByWorkshopTitleContainingIgnoreCase(String workshopTitle);

    List<TrainingSkillWorkshop> findByOrganizationContainingIgnoreCase(String organization);

    List<TrainingSkillWorkshop> findByWorkshopDate(LocalDate workshopDate);
}


