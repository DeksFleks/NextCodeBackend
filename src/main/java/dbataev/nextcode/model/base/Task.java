package dbataev.nextcode.model.base;

import dbataev.nextcode.model.manyToMany.LessonTask;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String question;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(columnDefinition = "jsonb")
    private String options;

    private String explanation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "task")
    private List<LessonTask> lessonTasks = new ArrayList<>();
}