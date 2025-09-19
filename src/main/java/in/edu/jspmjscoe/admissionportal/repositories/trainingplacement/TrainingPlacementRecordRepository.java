package in.edu.jspmjscoe.admissionportal.repositories.trainingplacement;

import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TrainingPlacementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainingPlacementRecordRepository extends JpaRepository<TrainingPlacementRecord, Long> {

    @Query("SELECT r FROM TrainingPlacementRecord r " +
            "JOIN FETCH r.student s " +
            "LEFT JOIN FETCH r.tests t " +
            "WHERE s.division = :division")
    List<TrainingPlacementRecord> findByDivision(String division);

    // find record by studentId
    Optional<TrainingPlacementRecord> findByStudent_StudentId(Long studentId);

}
