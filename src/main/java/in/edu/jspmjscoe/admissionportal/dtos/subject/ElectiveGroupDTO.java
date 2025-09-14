package in.edu.jspmjscoe.admissionportal.dtos.subject;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectiveGroupDTO {
    private Long groupId;
    private String name;
    private Integer maxPick;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Instead of exposing full Subject objects, only their IDs
    private List<Long> subjectIds;
}
