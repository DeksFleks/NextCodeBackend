package dbataev.nextcode.model.base;

import dbataev.nextcode.model.manyToMany.LessonTask;
import dbataev.nextcode.model.manyToMany.ModuleLesson;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "xp_reward")
    private Integer xpReward;

    @Column(name = "theory_text")
    private String theoryText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "lesson")
    private List<ModuleLesson> moduleLessons = new ArrayList<>();

    @OneToMany(mappedBy = "lesson")
    private List<LessonTask> lessonTasks = new ArrayList<>();
}