package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.Course;
import dbataev.nextcode.model.base.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
