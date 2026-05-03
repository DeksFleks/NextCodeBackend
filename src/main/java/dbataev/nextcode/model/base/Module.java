package dbataev.nextcode.model.base;

import dbataev.nextcode.model.manyToMany.ModuleLesson;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId;

    private String title;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "module")
    private List<ModuleLesson> moduleLessons = new ArrayList<>();

    public static List<Lesson> getLessons(List<Module> modules) {
        List<Lesson> lessons = new ArrayList<>();

        for (Module module : modules) {
            for (ModuleLesson moduleLesson : module.getModuleLessons()) {
                lessons.add(moduleLesson.getLesson());
            }
        }

        return lessons;
    }
}
