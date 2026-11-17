package dbataev.nextcode.model.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "coding_tasks")
public class CodingTask {
    @Id
    @Column(name = "task_id")
    private Long id;
    @Column(name = "starter_code")
    private String starterCode;
    @Column(name = "solution_code")
    private String solutionCode;
    @Column(name = "programming_language")
    private String programmingLanguage;
}
