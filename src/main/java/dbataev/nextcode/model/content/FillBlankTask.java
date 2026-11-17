package dbataev.nextcode.model.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "fill_blank_tasks")
public class FillBlankTask {
    @Id
    @Column(name = "task_id")
    private Long id;
    @Column(name = "sentence_before")
    private String sentenceBefore;
    @Column(name = "sentence_after")
    private String sentenceAfter;
    @Column(name = "correct_answer")
    private String correctAnswer;
    @Column(name = "case_sensitive")
    private Boolean caseSensitive;
}
