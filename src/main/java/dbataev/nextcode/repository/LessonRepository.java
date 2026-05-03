package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
