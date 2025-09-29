package in.edu.jspmjscoe.admissionportal.repositories.subject;

import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByHasCCETrue();

}
