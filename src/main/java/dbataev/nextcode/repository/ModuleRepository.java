package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByCourseIdAndIsDeletedFalseOrderByOrderIndexAsc(Long courseId);

    @Query("""
    SELECT DISTINCT m
    FROM modules m
    LEFT JOIN FETCH m.moduleLessons ml
    LEFT JOIN FETCH ml.lesson l
    WHERE m.courseId = :courseId
      AND m.isDeleted = false
    ORDER BY m.orderIndex ASC, ml.orderIndex ASC
""")
    List<Module> findModulesWithLessonsByCourseId(@Param("courseId") Long courseId);
}