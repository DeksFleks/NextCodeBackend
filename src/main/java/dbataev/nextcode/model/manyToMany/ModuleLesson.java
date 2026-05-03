package dbataev.nextcode.model.manyToMany;

import dbataev.nextcode.model.base.Module;
import dbataev.nextcode.model.base.Lesson;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "module_lessons")
public class ModuleLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "order_index")
    private Integer orderIndex;
}
