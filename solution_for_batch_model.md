# Solution: Implementing a `Batch` Model

This document outlines the plan to introduce a `Batch` entity to the existing data model. The goal is to better organize students by their academic group (year, semester, division) and to create a clear link between students and the subjects they are enrolled in for any given term.

## 1. The Problem

Currently, the `Student` model has fields like `division` and is linked to a `Course`. The `Subject` model contains `yearOfStudy` and `semester`. While functional, this structure makes it difficult to:
- Track a student's progression through different semesters and years.
- Manage the specific set of subjects assigned to a group of students in a particular semester.
- Query for all students in a specific batch (e.g., "Third Year, Semester 5, Division A").

## 2. Proposed Solution: The `Batch` Model

A new `Batch` entity will be created to act as a central hub connecting `Course`, `Student`, and `Subject` entities for a specific academic period.

### A. New `Batch.java` Entity

A new file, `Batch.java`, should be created in a new package `in.edu.jspmjscoe.admissionportal.model.batch`.

```java
package in.edu.jspmjscoe.admissionportal.model.batch;

import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @Column(nullable = false)
    private int startYear; // e.g., 2023

    @Column(nullable = false)
    private int endYear;   // e.g., 2027

    @Column(nullable = false)
    private int currentSemester;

    @Column(nullable = false)
    private String division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "batch")
    private Set<Student> students = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "batch_subjects",
        joinColumns = @JoinColumn(name = "batch_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();
}
```

### B. Modifications to Existing Models

To integrate the `Batch` model, the following changes are required in `Student.java` and `Subject.java`.

#### `Student.java`
Add a `ManyToOne` relationship to `Batch`. This field will represent the student's current batch assignment.

**Add the import:**
```java
import in.edu.jspmjscoe.admissionportal.model.batch.Batch;
```

**Add the field to the `Student` class:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "batch_id")
private Batch batch;
```

#### `Subject.java`
To complete the bidirectional relationship defined in `Batch`, add a `ManyToMany` field that maps back to the `Batch` entity.

**Add the import:**
```java
import in.edu.jspmjscoe.admissionportal.model.batch.Batch;
```

**Add the field to the `Subject` class:**
```java
@ManyToMany(mappedBy = "subjects")
private Set<Batch> batches = new HashSet<>();
```

## 3. Benefits of this Approach

- **Centralized Logic:** The `Batch` model provides a single source of truth for what constitutes an academic group.
- **Clear Relationships:** It explicitly defines the many-to-many relationship between batches and subjects, and the many-to-one relationship between students and their current batch.
- **Improved Querying:** It will be easier to write queries to fetch all students in a batch, or all subjects for a batch.
- **Scalability:** This model can be extended to track student history by creating a join table between `Student` and `Batch` to record all batches a student has been a part of over time.
