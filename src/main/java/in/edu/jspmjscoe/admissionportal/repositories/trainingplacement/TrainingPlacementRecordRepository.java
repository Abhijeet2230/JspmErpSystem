package in.edu.jspmjscoe.admissionportal.repositories.trainingplacement;

import in.edu.jspmjscoe.admissionportal.model.trainingplacement.TrainingPlacementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainingPlacementRecordRepository extends JpaRepository<TrainingPlacementRecord, Long> {

    // find all records for a division
    List<TrainingPlacementRecord> findByStudentAcademicYear_Division(String division);

    // find by StudentAcademicYear ID
    Optional<TrainingPlacementRecord> findByStudentAcademicYear_StudentAcademicYearId(Long studentAcademicYearId);


    // Fetch all training & placement records for a given rollNo
    List<TrainingPlacementRecord> findByStudentAcademicYear_RollNo(Integer rollNo);


}
