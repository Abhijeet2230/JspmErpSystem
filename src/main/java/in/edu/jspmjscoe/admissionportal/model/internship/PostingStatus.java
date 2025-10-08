package in.edu.jspmjscoe.admissionportal.model.internship;

public enum PostingStatus {
    DRAFT,      // Initial state when creating posting
    ACTIVE,     // Available for applications
    CLOSED,     // No longer accepting applications
    CANCELLED   // Posting was cancelled
}
