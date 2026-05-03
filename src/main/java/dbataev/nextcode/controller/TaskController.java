package dbataev.nextcode.controller;

import dbataev.nextcode.model.base.Task;
import dbataev.nextcode.model.dto.TaskDto;
import dbataev.nextcode.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "/bylesson/{lessonId}")
    public List<TaskDto> getByLessonId(@PathVariable Long lessonId) {
        return taskService.findLessonTasks(lessonId);
    }
}
