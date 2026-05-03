package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.Lesson;
import dbataev.nextcode.model.manyToMany.LessonTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonTaskRepository extends JpaRepository<LessonTask, Long> {
    List<LessonTask> findByLessonId(Long lessonId);
}
