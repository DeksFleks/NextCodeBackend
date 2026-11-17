package dbataev.nextcode.model.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "multiple_choice_tasks")
public class MultipleChoiceTask {
    @Id
    @Column(name = "task_id")
    private Long id;
    @Column(name = "is_multiple_select")
    private Boolean isMultipleSelect;
    @OneToMany
    @JoinColumn(name = "task_id")
    @OrderBy("orderIndex ASC, id ASC")
    private List<MultipleChoiceOption> options;
}
