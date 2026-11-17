package dbataev.nextcode.model.base;
import dbataev.nextcode.model.content.*;
import dbataev.nextcode.model.manyToMany.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Entity(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    private String type;
    @OneToMany
    @JoinColumn(name = "task_id")
    private List<TaskTranslation> translations;
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "task_id", insertable = false, updatable = false)
    private MultipleChoiceTask multipleChoice;
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "task_id", insertable = false, updatable = false)
    private CodingTask coding;
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "task_id", insertable = false, updatable = false)
    private FillBlankTask fillBlank;
    @OneToMany(mappedBy = "task")
    private List<LessonTask> lessonTasks;
}
