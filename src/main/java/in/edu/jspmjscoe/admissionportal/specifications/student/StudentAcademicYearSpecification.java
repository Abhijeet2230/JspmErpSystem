package in.edu.jspmjscoe.admissionportal.specifications.student;

import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class StudentAcademicYearSpecification {

    public static Specification<StudentAcademicYear> filterBy(
            Integer yearOfStudy,
            Integer semester,
            String department,
            String course,
            String division
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (yearOfStudy != null) {
                predicates.add(criteriaBuilder.equal(root.get("yearOfStudy"), yearOfStudy));
            }

            if (semester != null) {
                predicates.add(criteriaBuilder.equal(root.get("semester"), semester));
            }

            if (department != null && !department.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("student").get("homeUniversity"), department));
            }

            if (course != null && !course.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("course").get("name"), course));
            }

            if (division != null && !division.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("division"), division));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
