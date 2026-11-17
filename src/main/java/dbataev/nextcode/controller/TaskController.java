package dbataev.nextcode.controller;
import dbataev.nextcode.model.dto.*;
import dbataev.nextcode.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService tasks;
    public TaskController(TaskService tasks) { this.tasks = tasks; }
    @GetMapping("/bylesson/{lessonId}")
    public List<TaskDto> getByLessonId(@PathVariable Long lessonId, Authentication auth) {
        return tasks.findLessonTasks(lessonId, (Long) auth.getPrincipal());
    }
    @PostMapping("/{taskId}/check")
    public TaskResult check(@PathVariable Long taskId, Authentication auth, @Valid @RequestBody TaskAnswer answer) {
        return tasks.check(taskId, (Long) auth.getPrincipal(), answer);
    }
}
