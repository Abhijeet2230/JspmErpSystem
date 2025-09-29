package in.edu.jspmjscoe.admissionportal.model.achievements;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mini_project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MiniProject extends BaseProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mini_project_id")
    private Long miniProjectId;



    // extra fields for mini project if needed
}
