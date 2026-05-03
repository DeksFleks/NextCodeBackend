package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
