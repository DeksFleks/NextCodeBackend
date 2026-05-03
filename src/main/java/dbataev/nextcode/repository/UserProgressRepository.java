package dbataev.nextcode.repository;

import dbataev.nextcode.model.manyToMany.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    @Query(value = """
    SELECT up.lesson_id
    FROM user_progress up
    JOIN module_lessons ml ON up.lesson_id = ml.lesson_id
    JOIN modules m ON ml.module_id = m.id
    WHERE up.user_id = :userId
      AND m.course_id = :courseId
      AND up.is_completed = true
    """, nativeQuery = true)
    List<Long> findCompletedLessonIdsByUserIdAndCourseId(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId
    );
}
