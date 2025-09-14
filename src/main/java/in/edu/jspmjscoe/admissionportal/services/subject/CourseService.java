package in.edu.jspmjscoe.admissionportal.services.subject;

import in.edu.jspmjscoe.admissionportal.model.subject.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course saveCourse(Course course);
    Optional<Course> getCourseById(Long id);
    List<Course> getAllCourses();
    Course updateCourse(Long id, Course updatedCourse);
    void deleteCourse(Long id);
}
