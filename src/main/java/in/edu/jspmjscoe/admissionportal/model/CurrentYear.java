package in.edu.jspmjscoe.admissionportal.model;

import lombok.Getter;

@Getter
public enum CurrentYear {
    FIRST_YEAR("FY"),
    SECOND_YEAR("SY"),
    THIRD_YEAR("TY"),
    FINAL_YEAR("Final");

    private final String displayName;

    CurrentYear(String displayName) {
        this.displayName = displayName;
    }

}
