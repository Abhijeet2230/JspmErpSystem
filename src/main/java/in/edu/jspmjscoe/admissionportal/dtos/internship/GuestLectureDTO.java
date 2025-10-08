package in.edu.jspmjscoe.admissionportal.dtos.internship;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestLectureDTO {

    private Long id;
    private String lectureTitle;
    private String speakerName;
    private String organization;
    private String venue;
    private LocalDate lectureDate;
    private Double durationHours;
    private Boolean certificateProvided;
}
