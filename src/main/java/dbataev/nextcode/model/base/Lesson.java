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
@Entity(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "xp_reward")
    private Integer xpReward;
    @OneToMany
    @JoinColumn(name = "lesson_id")
    private List<LessonTranslation> translations;
    @OneToMany(mappedBy = "lesson")
    private List<ModuleLesson> moduleLessons;
    @OneToMany(mappedBy = "lesson")
    private List<LessonTask> lessonTasks;
}
