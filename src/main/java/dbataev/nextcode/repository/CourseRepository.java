package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
