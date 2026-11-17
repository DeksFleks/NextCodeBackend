package dbataev.nextcode.service;
import dbataev.nextcode.model.base.Task;
import dbataev.nextcode.model.content.*;
import dbataev.nextcode.model.dto.*;
import dbataev.nextcode.model.mapper.*;
import dbataev.nextcode.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@Service
@Transactional(readOnly = true)
public class TaskService {
    private final LessonTaskRepository links;
    private final LessonRepository lessons;
    private final ContentLanguageService languages;
    private final JdbcTemplate jdbc;
    public TaskService(LessonTaskRepository links, LessonRepository lessons, ContentLanguageService languages, JdbcTemplate jdbc) {
        this.links = links; this.lessons = lessons; this.languages = languages; this.jdbc = jdbc;
    }
    private List<Task> tasks(Long lessonId) {
        lessons.findById(lessonId).filter(l -> !Boolean.TRUE.equals(l.getIsDeleted()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));
        return links.findByLessonIdOrderByOrderIndexAscIdAsc(lessonId).stream()
            .map(l -> l.getTask()).filter(t -> !Boolean.TRUE.equals(t.getIsDeleted())).toList();
    }
    public List<TaskDto> findLessonTasks(Long lessonId, Long userId) {
        Long language = languages.forUser(userId);
        return tasks(lessonId).stream().map(t -> TaskMapper.toDto(t, language)).toList();
    }
    @Transactional
    public TaskResult check(Long taskId, Long userId, TaskAnswer request) {
        Task task = tasks(request.lessonId()).stream().filter(t -> t.getId().equals(taskId)).findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not in lesson"));
        boolean correct;
        String mode = "EXACT_TEXT";
        switch(task.getType()) {
            case "MULTIPLE_CHOICE" -> {
                var options = task.getMultipleChoice().getOptions();
                var submitted = request.optionIds() == null ? List.<Long>of() : request.optionIds();
                var selected = new HashSet<>(submitted);
                var allowed = options.stream().map(MultipleChoiceOption::getId).toList();
                if (selected.size() != submitted.size() || !allowed.containsAll(selected) ||
                    (!Boolean.TRUE.equals(task.getMultipleChoice().getIsMultipleSelect()) && selected.size() > 1)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid option selection");
                }
                var expected = new HashSet<>(options.stream().filter(o -> Boolean.TRUE.equals(o.getIsCorrect())).map(MultipleChoiceOption::getId).toList());
                correct = !selected.isEmpty() && selected.equals(expected);
                mode = "OPTION_SET";
            }
            case "FILL_BLANK" -> {
                var fill = task.getFillBlank();
                correct = request.answer() != null && (Boolean.TRUE.equals(fill.getCaseSensitive())
                    ? fill.getCorrectAnswer().strip().equals(request.answer().strip())
                    : fill.getCorrectAnswer().strip().equalsIgnoreCase(request.answer().strip()));
            }
            case "CODING" -> {
                // MVP: compare source only. Never execute code inside the API process.
                correct = request.answer() != null && normalize(task.getCoding().getSolutionCode()).equals(normalize(request.answer()));
                mode = "EXACT_SOURCE";
            }
            default -> throw new ResponseStatusException(HttpStatus.CONFLICT, "Unsupported task type");
        }
        if (correct) jdbc.update("INSERT INTO task_attempts(user_id, lesson_id, task_id) VALUES (?, ?, ?) ON CONFLICT DO NOTHING",
            userId, request.lessonId(), taskId);
        var text = ContentTranslations.resolve(task.getTranslations(), TaskTranslation::getLanguageId, languages.forUser(userId));
        return new TaskResult(correct, text.getExplanation(), mode);
    }
    public void requirePassed(Long lessonId, Long userId) {
        var tasks = tasks(lessonId);
        var passed = new HashSet<>(jdbc.queryForList("SELECT task_id FROM task_attempts WHERE user_id = ? AND lesson_id = ?", Long.class, userId, lessonId));
        if (tasks.isEmpty() || tasks.stream().anyMatch(t -> !passed.contains(t.getId())))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Complete all lesson tasks first");
    }
    private String normalize(String source) { return source.replace("\r\n", "\n").strip(); }
}
