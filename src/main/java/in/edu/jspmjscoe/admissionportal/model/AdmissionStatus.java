package in.edu.jspmjscoe.admissionportal.model;

import lombok.Getter;

@Getter
public enum AdmissionStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    REJECTED("Rejected");

    private final String displayName;

    AdmissionStatus(String displayName) {
        this.displayName = displayName;
    }

}
