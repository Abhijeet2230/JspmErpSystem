package in.edu.jspmjscoe.admissionportal.services.subject;

import in.edu.jspmjscoe.admissionportal.model.subject.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    Subject saveSubject(Subject subject);
    Optional<Subject> getSubjectById(Long id);
    List<Subject> getAllSubjects();
    Subject updateSubject(Long id, Subject updatedSubject);
    void deleteSubject(Long id);
}
