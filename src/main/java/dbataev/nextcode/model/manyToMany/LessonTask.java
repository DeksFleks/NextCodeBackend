package dbataev.nextcode.model.manyToMany;

import dbataev.nextcode.model.base.Lesson;
import jakarta.persistence.*;
import lombok.Data;
import dbataev.nextcode.model.base.Task;

@Entity
@Table(name = "lesson_tasks")
@Data
public class LessonTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "order_index")
    private Integer orderIndex;
}