package dbataev.nextcode.service;

import dbataev.nextcode.model.dto.TaskDto;
import dbataev.nextcode.model.manyToMany.LessonTask;
import dbataev.nextcode.model.mapper.TaskMapper;
import dbataev.nextcode.repository.LessonRepository;
import dbataev.nextcode.repository.LessonTaskRepository;
import dbataev.nextcode.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final LessonRepository lessonRepository;
    private final LessonTaskRepository lessonTaskRepository;

    public TaskService(TaskRepository taskRepository, LessonRepository lessonRepository, LessonTaskRepository lessonTaskRepository) {
        this.taskRepository = taskRepository;
        this.lessonRepository = lessonRepository;
        this.lessonTaskRepository = lessonTaskRepository;
    }

    public List<TaskDto> findLessonTasks(Long lessonId) {
        List<LessonTask> lessonTasks = lessonTaskRepository.findByLessonId(lessonId);
        List<TaskDto> taskDtos = new ArrayList<>();

        for (LessonTask lessonTask : lessonTasks) {
            taskDtos.add(TaskMapper.toDto(lessonTask.getTask()));
        }

        return taskDtos;
    }
}
